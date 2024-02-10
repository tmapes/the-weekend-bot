package the.weekend.bot.repositories

import com.mongodb.client.model.Filters.text
import com.mongodb.client.model.ReplaceOptions
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import the.weekend.bot.entities.MovieWatchingEntity

@Singleton
class MovieWatchingRepository(
    private val movieCollection: CoroutineCollection<MovieWatchingEntity>
) {

    suspend fun getCountOfWatchedMovies(): Long {
        return movieCollection.countDocuments(
            MovieWatchingEntity::finished ne null
        )
    }

    suspend fun getCurrentlyWatchingMovie(): MovieWatchingEntity? {
        return movieCollection.findOne(
            MovieWatchingEntity::finished eq null
        )
    }

    suspend fun getMovieByNameAndYear(name: String, year: Int): MovieWatchingEntity? {
        return movieCollection.findOne(
            and(
                MovieWatchingEntity::name eq name,
                MovieWatchingEntity::year eq year
            )
        )
    }

    suspend fun saveMovie(movie: MovieWatchingEntity): Boolean {
        return movieCollection.replaceOne(
            and(
                MovieWatchingEntity::name eq movie.name,
                MovieWatchingEntity::year eq movie.year
            ),
            movie, ReplaceOptions().upsert(true)
        ).wasAcknowledged()
    }

    fun searchForWatchedMovie(searchText: String): Flow<MovieWatchingEntity> {
        return movieCollection.find(text(searchText)).toFlow()
    }
}
