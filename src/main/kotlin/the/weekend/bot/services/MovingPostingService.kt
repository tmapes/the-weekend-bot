package the.weekend.bot.services

import discord4j.discordjson.json.EmbedData
import discord4j.discordjson.json.EmbedFieldData
import discord4j.discordjson.json.EmbedImageData
import io.micronaut.context.annotation.Value
import kotlinx.coroutines.future.await
import org.slf4j.LoggerFactory
import the.weekend.bot.clients.MovieDatabaseClient
import the.weekend.bot.domains.Movie
import java.text.NumberFormat
import java.util.Currency
import javax.inject.Singleton

@Singleton
class MovingPostingService(
    private val discordEventService: DiscordEventService,
    private val movieDatabaseClient: MovieDatabaseClient,
    @Value("\${tmdb.api-key}") private val tmdbApiKey: String,
    @Value("\${discord.movie-channel-id}") private val movieChannelId: Long
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun postWatchedMovie(movie: Movie) {
        val tmdbMovie = try {
            movieDatabaseClient.getMovieById(movieId = movie.tmdbId, apiKey = tmdbApiKey).await().body()
        } catch (ex: Exception) {
            logger.warn("Failed to lookup '${movie.title} on rotten tomatoes", ex)
            null
        }

        val embedDataBuilder = EmbedData.builder()
            .title(movie.title)
            .url("https://www.themoviedb.org/movie/${movie.tmdbId}")
            .color(1234567)

        val director = tmdbMovie?.credits?.crew?.firstOrNull { it.isDirector() }
        director?.let {
            embedDataBuilder.addField(EmbedFieldData.builder().name("Directed by").value(it.name).build())
        }

        embedDataBuilder.addField(
            EmbedFieldData.builder()
                .name("Released")
                .value(movie.year.toString())
                .inline(true)
                .build()
        )

        if (tmdbMovie?.overview != null)
            embedDataBuilder.description(tmdbMovie.overview)
        if (tmdbMovie?.revenue != null)
            embedDataBuilder.addField(
                EmbedFieldData.builder().name("Box Office").value(CURRENCY_FORMAT.format(tmdbMovie.revenue)).build()
            )

        if (tmdbMovie?.images?.posters != null) {
            val image = tmdbMovie.images.posters.firstOrNull { it.isoCode == "en" } ?: tmdbMovie.images.posters.firstOrNull()

            image?.let {
                val url = "https://www.themoviedb.org/t/p/w1280/${it.filePath}"
                embedDataBuilder.image(
                    EmbedImageData.builder()
                        .url(url)
                        .build()
                )
            }
        }

        discordEventService.sendEmbedMessage(MOVIE_FINISHED_TEXT, embedDataBuilder.build(), movieChannelId)
    }

    companion object {
        const val MOVIE_FINISHED_TEXT = "**Finished a New Movie:**"

        private val CURRENCY_FORMAT = NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 0
            currency = Currency.getInstance("USD")
        }
    }
}
