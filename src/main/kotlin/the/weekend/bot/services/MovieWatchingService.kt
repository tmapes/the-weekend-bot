package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import the.weekend.bot.domains.Movie
import java.time.Clock
import java.time.Instant
import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
class MovieWatchingService(
    @Value("\${app.max-watch-size:100}") private val maximumWatchSize: Int,
    private val movieFetchService: MovieFetchService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private var currentMovie: Movie? = null
    private val watchedMovies: MutableSet<Movie> = mutableSetOf()

    @PostConstruct
    fun postConstruct() {
        getOrStartMovie()
    }

    fun getOrStartMovie(force: Boolean = false): Movie? {
        currentMovie?.let {
            val endTime = it.started.plus(it.length)
            val now = Instant.now(Clock.systemUTC())
            if (!force && now.isBefore(endTime)) {
                return it
            }
            logger.info("Movie is over: $currentMovie")
            watchedMovies.add(it)
        }

        if (watchedMovies.size >= maximumWatchSize)
            watchedMovies.clear()

        val newMovie = movieFetchService.getNextMovie()
        if (newMovie !in watchedMovies) {
            currentMovie = newMovie
            logger.info("Starting: $currentMovie")
            return currentMovie!!
        }

        currentMovie = null
        return currentMovie
    }
}
