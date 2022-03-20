package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import kotlinx.coroutines.future.await
import org.slf4j.LoggerFactory
import the.weekend.bot.clients.MovieDatabaseClient
import the.weekend.bot.domains.Movie
import the.weekend.bot.mappers.TmdbMovieMapper

@Singleton
class MoviePostingService(
    private val discordEventService: DiscordEventService,
    private val movieDatabaseClient: MovieDatabaseClient,
    @Value("\${tmdb.api-key}") private val tmdbApiKey: String,
    @Value("\${discord.movie-channel-id}") private val movieChannelId: Long
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun postStartingMovie(movie: Movie) {
        val tmdbMovie = try {
            movieDatabaseClient.getMovieById(movieId = movie.tmdbId, apiKey = tmdbApiKey).await().body()
        } catch (ex: Exception) {
            logger.warn("Failed to lookup '${movie.title} (id: ${movie.tmdbId}) on TMBD", ex)
            null
        }

        val embedData = TmdbMovieMapper.toEmbedData(movie, tmdbMovie)

        discordEventService.sendEmbedMessage(MOVIE_STARTING_TEXT, embedData, movieChannelId)
    }

    companion object {
        const val MOVIE_STARTING_TEXT = "**Starting a New Movie:**"
    }
}
