package info.mapes.weekend.services

import info.mapes.weekend.domains.Movie
import info.mapes.weekend.repositories.MovieWatchingRepository
import info.mapes.weekend.utils.letOrElse
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Clock
import java.time.Instant

@Singleton
class MovieWatchingService(
    private val movieFetchService: MovieFetchService,
    private val moviePostingService: MoviePostingService,
    private val movieWatchingRepository: MovieWatchingRepository,
) : ApplicationEventListener<ServerStartupEvent> {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private var currentMovie: Movie? = null

    override fun onApplicationEvent(ignored: ServerStartupEvent) =
        runBlocking {
            movieWatchingRepository.getCurrentlyWatchingMovie().letOrElse(
                nonNull = {
                    currentMovie = it.toMovie()
                    logger.info("Resuming Previous Movie: $it")
                },
                ifNull = { logger.info("Found no movie to resume.") },
            )
        }

    suspend fun getOrStartMovie(force: Boolean = false): Movie? {
        currentMovie?.let {
            val endTime = it.started.plus(it.length)
            val now = Instant.now(Clock.systemUTC())
            if (!force && now.isBefore(endTime)) {
                return it
            }
            logger.info("Movie is over: $currentMovie, now: $now, endTime: $endTime")
            movieWatchingRepository.saveMovie(it.toMovieEntity(now))
            currentMovie = null
        }

        try {
            movieFetchService.getNextMovie()?.let {
                currentMovie = it
                logger.info("Starting: $currentMovie")
                val success = movieWatchingRepository.saveMovie(it.toMovieEntity(null))
                if (success) {
                    moviePostingService.postStartingMovie(it)
                    return currentMovie
                }
                return null
            }
            currentMovie = null
            return null
        } catch (ex: Exception) {
            logger.error("Failed to retrieve a new movie to watch.", ex)
            currentMovie = null
            return null
        }
    }
}
