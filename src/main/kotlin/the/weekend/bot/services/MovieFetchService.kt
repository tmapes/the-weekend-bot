package the.weekend.bot.services

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import the.weekend.bot.clients.MovieDatabaseClient
import the.weekend.bot.domains.Movie
import the.weekend.bot.repositories.MovieWatchingRepository
import java.time.LocalDate

@Singleton
class MovieFetchService(
    private val movieDatabaseClient: MovieDatabaseClient,
    private val movieWatchingRepository: MovieWatchingRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var maxPage: Int = 500 // Start at 500 and dynamically change based on TMDB responses.

    suspend fun getNextMovie(): Movie? {
        val page = (1..maxPage).random()
        val discoveredMovies = movieDatabaseClient.topRatedMovies(page = page)

        if (maxPage != discoveredMovies.totalPages) {
            logger.info("Max TMDB page updated to ${discoveredMovies.totalPages}")
        }
        maxPage = discoveredMovies.totalPages // update maxPage to the page returned by TMDB
        if (discoveredMovies.results.isEmpty()) {
            logger.error("Null/Empty Body from TMDB, body: '$discoveredMovies'")
            return null
        }

        val discoveredMovie =
            discoveredMovies.results.firstOrNull {
                it.releaseDate.isAfter(EARLIEST_MOVIE_RELEASE) &&
                    movieWatchingRepository.getMovieByTmdbId(it.id) == null
            } ?: return null

        try {
            val nextMovie =
                movieDatabaseClient.getMovieById(discoveredMovie.id, appendToResponse = emptySet()).body()
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
