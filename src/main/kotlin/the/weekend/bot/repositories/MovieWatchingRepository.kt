package the.weekend.bot.repositories

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.ReplaceOptions
import jakarta.inject.Singleton
import org.litote.kmongo.and
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import the.weekend.bot.entities.MovieWatchingEntity

@Singleton
class MovieWatchingRepository(
    private val movieCollection: MongoCollection<MovieWatchingEntity>
) {

    fun getCurrentlyWatchingMovie(): MovieWatchingEntity? {
        return movieCollection.findOne {
            MovieWatchingEntity::finished eq null
        }
    }

    fun getMovieByNameAndYear(name: String, year: Int): MovieWatchingEntity? {
        return movieCollection.findOne {
            and(
                MovieWatchingEntity::name eq name,
                MovieWatchingEntity::year eq year
            )
        }
    }

    fun saveMovie(movie: MovieWatchingEntity): Boolean {
        return movieCollection.replaceOne(
            and(
                MovieWatchingEntity::name eq movie.name,
                MovieWatchingEntity::year eq movie.year
            ),
            movie, ReplaceOptions().upsert(true)
        ).wasAcknowledged()
    }
}
