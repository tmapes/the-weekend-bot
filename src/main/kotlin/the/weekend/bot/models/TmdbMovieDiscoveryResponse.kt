package the.weekend.bot.models

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.LocalDate

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbMovieDiscoveryResponse(
    val results: List<TmdbDiscoveredMovie>,
    val totalPages: Int
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbDiscoveredMovie(
    val id: Int,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbMovie(
    val id: Int,
    val title: String,
    val runtime: Long,
    @JsonFormat(pattern = "yyyy-MM-dd") val releaseDate: LocalDate,
    val tagline: String = "",
    val credits: TmdbCreditResponse?,
    val overview: String?,
    val revenue: Long?,
    val budget: Long?,
    val images: TmdbImages?
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbCreditResponse(
    val crew: List<TmdbCredit>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbCredit(
    val name: String,
    val job: String
) {
    val director = "DIRECTOR" == job.uppercase()
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbImages(
    val posters: List<TmdbImage>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TmdbImage(
    @JsonProperty("iso_639_1") val isoCode: String?,
    val filePath: String
)
