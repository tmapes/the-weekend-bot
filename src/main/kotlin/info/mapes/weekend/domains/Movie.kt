package info.mapes.weekend.domains

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.util.StdDateFormat
import info.mapes.weekend.entities.MovieWatchingEntity
import info.mapes.weekend.models.TmdbMovie
import java.time.Clock
import java.time.Duration
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Movie(
    val title: String,
    val year: Int,
    val length: Duration,
    @JsonFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601, timezone = "UTC")
    val started: Instant,
    val tmdbId: Int,
) {
    fun toDiscordText(): String {
        return "$title ($year)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (title != other.title) return false
        if (year != other.year) return false
        if (length != other.length) return false
        if (started != other.started) return false
        if (tmdbId != other.tmdbId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + year
        result = 31 * result + length.hashCode()
        result = 31 * result + started.hashCode()
        result = 31 * result + tmdbId
        return result
    }

    fun toMovieEntity(finished: Instant?): MovieWatchingEntity {
        return MovieWatchingEntity(
            name = title,
            year = year,
            length = length.toMinutes(),
            tmdbId = tmdbId,
            started = started,
            finished = finished,
        )
    }

    constructor(movie: TmdbMovie) : this(
        title = movie.title,
        year = movie.releaseDate.year,
        length = Duration.ofMinutes(movie.runtime),
        started = Instant.now(Clock.systemUTC()),
        tmdbId = movie.id,
    )
}
