package the.weekend.bot.entities

import the.weekend.bot.domains.Movie
import java.time.Duration
import java.time.Instant

data class MovieWatchingEntity(
    val name: String,
    val year: Int,
    val length: Long,
    val tmdbId: Int,
    val started: Instant,
    val finished: Instant?,
) {
    fun toMovie(): Movie {
        return Movie(
            title = name,
            year = year,
            length = Duration.ofMinutes(length),
            started = started,
            tmdbId = tmdbId,
        )
    }

    override fun toString(): String {
        return "MovieWatchingEntity(name='$name' ($year), length=$length, tmdbId=$tmdbId, started=$started, finished=$finished)"
    }
}
