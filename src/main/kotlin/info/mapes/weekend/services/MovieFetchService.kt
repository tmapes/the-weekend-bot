package info.mapes.weekend.services

import info.mapes.weekend.clients.MovieDatabaseClient
import info.mapes.weekend.domains.Movie
import info.mapes.weekend.repositories.MovieWatchingRepository
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MovieFetchService(
    private val movieDatabaseClient: MovieDatabaseClient,
    private val movieWatchingRepository: MovieWatchingRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun getNextMovie(): Movie? {
        val page = (1..MAX_PAGE).random()
        val discoveredMovies =
            try {
                movieDatabaseClient.discoverMovies(page = page)
            } catch (ex: HttpClientResponseException) {
                val responseBody = ex.response.getBody(String::class.java).orElse("<no body>")
                logger.error("Received status '${ex.status.code}' from discover call. Response Body:\n$responseBody", ex)
                return null
            }

        if (discoveredMovies.results.isEmpty()) {
            logger.error("Null/Empty Body from TMDB, body: '$discoveredMovies'")
            return null
        }

        val discoveredMovie =
            discoveredMovies.results.firstOrNull {
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
        // Despite having more than 500 pages of movies available, the TMDB api doesn't allow going above that.
        private const val MAX_PAGE: Int = 500
    }
}
