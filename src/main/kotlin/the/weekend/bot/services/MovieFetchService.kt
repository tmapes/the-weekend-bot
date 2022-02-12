package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpStatus
import kotlinx.coroutines.future.await
import org.slf4j.LoggerFactory
import the.weekend.bot.clients.MovieDatabaseClient
import the.weekend.bot.domains.Movie
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class MovieFetchService(
    @Value("\${tmdb.api-key}") private val tmdbApiKey: String,
    private val movieDatabaseClient: MovieDatabaseClient
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun getNextMovie(): Movie? {
        val page = (0..499).random()
        val discoveredMovies = movieDatabaseClient.topRatedMovies(apiKey = tmdbApiKey, page = page).await()

        if (discoveredMovies.status != HttpStatus.OK) {
            logger.warn("Non OK status from TMDB: ${discoveredMovies.status}")
        }
        val body = discoveredMovies.body()
        if (body == null || body.results.isEmpty()) {
            logger.error("Null/Empty Body from TMDB : Status: '${discoveredMovies.status}' and body: $body")
            return null
        }
        val discoveredMovie = run {
            var shouldWatch = body.results.random()
            while (shouldWatch.releaseDate.isBefore(EARLIEST_MOVIE_RELEASE)) {
                shouldWatch = body.results.random()
            }
            shouldWatch
        }
        try {
            val nextMovie =
                movieDatabaseClient.getMovieById(movieId = discoveredMovie.id, appendToResponse = emptySet(), apiKey = tmdbApiKey).await().body()
                    ?: run {
                        logger.error("Failed to get data for $discoveredMovie when performing lookup...")
                        return null
                    }
            return Movie(nextMovie)
        } catch (ex: Exception) {
            logger.error("Uber failed to get data for $discoveredMovie", ex)
        }
        return null
    }

    companion object {
        private val EARLIEST_MOVIE_RELEASE = LocalDate.of(1960, 1, 1)
    }
}
