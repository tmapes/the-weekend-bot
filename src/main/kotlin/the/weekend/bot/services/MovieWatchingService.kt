package the.weekend.bot.services

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import the.weekend.bot.domains.Movie
import the.weekend.bot.repositories.MovieWatchingRepository
import java.time.Clock
import java.time.Instant
import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
class MovieWatchingService(
    private val movieFetchService: MovieFetchService,
    private val moviePostingService: MovingPostingService,
    private val movieWatchingRepository: MovieWatchingRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private var currentMovie: Movie? = null

    @PostConstruct
    fun postConstruct() {
        currentMovie = movieWatchingRepository.getCurrentlyWatchingMovie()?.toMovie()
        currentMovie?.let {
            logger.info("Resuming Previous Movie: $it")
        }
    }

    suspend fun getOrStartMovie(force: Boolean = false): Movie? {
        currentMovie?.let {
            val endTime = it.started.plus(it.length)
            val now = Instant.now(Clock.systemUTC())
            if (!force && now.isBefore(endTime)) {
                return it
            }
            logger.info("Movie is over: $currentMovie")
            movieWatchingRepository.saveMovie(it.toMovieEntity(now))
            moviePostingService.postWatchedMovie(it)
            currentMovie = null
        }

        try {
            val newMovie = movieFetchService.getNextMovie()
            newMovie?.let {
                val existingMovie = movieWatchingRepository.getMovieByNameAndYear(it.title, it.year)
                if (existingMovie != null) {
                    logger.info("Already watched $it")
                    return null
                }
                currentMovie = newMovie
                logger.info("Starting: $currentMovie")
                val success = movieWatchingRepository.saveMovie(it.toMovieEntity(null))
                if (success)
                    return currentMovie
                return null
            }
            currentMovie = null
            return currentMovie
        } catch (ex: Exception) {
            logger.error("Failed to retrieve a new movie to watch.", ex)
            currentMovie = null
            return null
        }
    }
}
