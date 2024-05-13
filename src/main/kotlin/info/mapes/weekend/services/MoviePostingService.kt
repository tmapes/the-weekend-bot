package info.mapes.weekend.services

import info.mapes.weekend.clients.MovieDatabaseClient
import info.mapes.weekend.domains.Movie
import info.mapes.weekend.mappers.TmdbMovieMapper
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MoviePostingService(
    private val discordEventService: DiscordEventService,
    private val movieDatabaseClient: MovieDatabaseClient,
    @Value("\${discord.movie-channel-id}") private val movieChannelId: Long,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun postStartingMovie(movie: Movie) {
        val tmdbMovie =
            try {
                movieDatabaseClient.getMovieById(movie.tmdbId).body()
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
