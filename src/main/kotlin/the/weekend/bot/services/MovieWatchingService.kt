package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import the.weekend.bot.domains.Movie
import java.time.Clock
import java.time.Instant
import javax.inject.Singleton

@Singleton
class MovieWatchingService(
    @Value("\${app.max-watch-size:100}") private val maximumWatchSize: Int,
    private val movieFetchService: MovieFetchService,
    private val moviePostingService: MovingPostingService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private var currentMovie: Movie? = null
    private val watchedMovies: MutableSet<Movie> = mutableSetOf()

    suspend fun getOrStartMovie(force: Boolean = false): Movie? {
        currentMovie?.let {
            val endTime = it.started.plus(it.length)
            val now = Instant.now(Clock.systemUTC())
            if (!force && now.isBefore(endTime)) {
                return it
            }
            logger.info("Movie is over: $currentMovie")
            watchedMovies.add(it)
            moviePostingService.postWatchedMovie(it)
        }

        if (watchedMovies.size >= maximumWatchSize)
            watchedMovies.clear()

        try {
            val newMovie = movieFetchService.getNextMovie()
            if (newMovie !in watchedMovies) {
                currentMovie = newMovie
                logger.info("Starting: $currentMovie")
                return currentMovie
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
