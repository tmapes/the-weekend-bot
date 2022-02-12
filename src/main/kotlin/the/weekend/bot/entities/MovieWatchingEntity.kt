package the.weekend.bot.entities

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import the.weekend.bot.domains.Movie
import java.time.Duration
import java.time.Instant

data class MovieWatchingEntity(
    @BsonId
    val _id: ObjectId = ObjectId(),
    val name: String,
    val year: Int,
    val length: Long,
    val tmdbId: Int,
    val started: Instant,
    val finished: Instant?
) {
    fun toMovie(): Movie {
        return Movie(
            title = name,
            year = year,
            length = Duration.ofMinutes(length),
            started = started,
            tmdbId = tmdbId
        )
    }
}
