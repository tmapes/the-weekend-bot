package the.weekend.bot.mappers

import discord4j.discordjson.json.EmbedData
import discord4j.discordjson.json.EmbedFieldData
import discord4j.discordjson.json.EmbedImageData
import the.weekend.bot.domains.Movie
import the.weekend.bot.models.TmdbCredit
import the.weekend.bot.models.TmdbMovie
import java.text.NumberFormat
import java.util.Currency

class TmdbMovieMapper {

    companion object {
        private const val GREEN: Int = 1234567
        private val CURRENCY_FORMAT = NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 0
            currency = Currency.getInstance("USD")
        }

        @JvmStatic
        fun toEmbedData(movie: Movie, tmdbMovie: TmdbMovie?): EmbedData {

            val embedDataBuilder = EmbedData.builder()
                .title(movie.title)
                .url("https://www.themoviedb.org/movie/${movie.tmdbId}")
                .color(GREEN)
                .addField(
                    EmbedFieldData.builder()
                        .name("Released")
                        .value(movie.year.toString())
                        .build()
                )

            if (tmdbMovie == null)
                return embedDataBuilder.build()

            tmdbMovie.credits?.crew?.firstOrNull(TmdbCredit::director)?.let {
                embedDataBuilder.addField(EmbedFieldData.builder().name("Directed by").value(it.name).build())
            }

            tmdbMovie.overview?.let {
                embedDataBuilder.description(it)
            }

            tmdbMovie.revenue?.let {
                if (it > 0)
                    embedDataBuilder.addField(
                        EmbedFieldData.builder().name("Box Office").value(CURRENCY_FORMAT.format(tmdbMovie.revenue))
                            .build()
                    )
            }

            tmdbMovie.budget?.let {
                if (it > 0)
                    embedDataBuilder.addField(
                        EmbedFieldData.builder().name("Budget").value(CURRENCY_FORMAT.format(tmdbMovie.budget)).build()
                    )
            }

            if (tmdbMovie.images?.posters != null) {
                val image = tmdbMovie.images.posters.firstOrNull { it.isoCode == "en" }
                    ?: tmdbMovie.images.posters.firstOrNull()

                image?.let {
                    val url = "https://www.themoviedb.org/t/p/w1280/${it.filePath}"
                    embedDataBuilder.image(
                        EmbedImageData.builder()
                            .url(url)
                            .build()
                    )
                }
            }

            return embedDataBuilder.build()
        }
    }
}
