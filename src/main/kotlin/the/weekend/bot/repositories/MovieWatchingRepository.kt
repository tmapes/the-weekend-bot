package the.weekend.bot.repositories

import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import the.weekend.bot.entities.MovieWatchingEntity
import javax.inject.Singleton

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
            MovieWatchingEntity::name eq name
            MovieWatchingEntity::year eq year
        }
    }

    fun saveMovie(movie: MovieWatchingEntity): Boolean {
        return movieCollection.insertOne(movie).wasAcknowledged()
    }
}
