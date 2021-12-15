package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import the.weekend.bot.clients.MovieDatabaseClient
import the.weekend.bot.domains.Movie
import javax.inject.Singleton

@Singleton
class MovieFetchService(
    @Value("\${tmdb.api-key}") private val tmdbApiKey: String,
    private val movieDatabaseClient: MovieDatabaseClient
) {

    fun getNextMovie(): Movie? {
        val page = (0..1000).random()
        val discoveredMovies = movieDatabaseClient.discoverMovie(apiKey = tmdbApiKey, page = page)
        if (discoveredMovies == null || discoveredMovies.results.isEmpty()) {
            return null
        }
        val discoveredMovie = discoveredMovies.results.random()

        val nextMovie = movieDatabaseClient.getMovieById(movieId = discoveredMovie.id, apiKey = tmdbApiKey) ?: return null
        return Movie(nextMovie)
    }
}
