package the.weekend.bot.domains

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.util.StdDateFormat
import the.weekend.bot.configs.MovieConfiguration
import java.time.Clock
import java.time.Duration
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Movie(
    val title: String,
    val year: Int,
    val length: Duration,
    @JsonFormat(pattern = StdDateFormat.DATE_FORMAT_STR_ISO8601, timezone = "UTC")
    val started: Instant
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

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + year
        return result
    }

    constructor(movieConfiguration: MovieConfiguration) : this(
        title = movieConfiguration.title,
        year = movieConfiguration.year,
        length = movieConfiguration.length,
        started = Instant.now(Clock.systemUTC())
    )
}
