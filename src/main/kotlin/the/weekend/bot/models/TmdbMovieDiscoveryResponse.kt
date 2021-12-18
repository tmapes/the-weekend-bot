package the.weekend.bot.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbMovieDiscoveryResponse(
    val results: List<TmdbDiscoveredMovie>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbDiscoveredMovie(
    val id: Int,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbMovie(
    val title: String,
    val runtime: Long,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate
)
