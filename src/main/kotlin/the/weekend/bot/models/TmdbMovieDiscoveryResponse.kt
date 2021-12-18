package the.weekend.bot.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDate

data class TmdbMovieDiscoveryResponse(
    val results: List<TmdbDiscoveredMovie>
)

data class TmdbDiscoveredMovie(
    val id: Int,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate
)

data class TmdbMovie(
    val title: String,
    val runtime: Long,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate
)
