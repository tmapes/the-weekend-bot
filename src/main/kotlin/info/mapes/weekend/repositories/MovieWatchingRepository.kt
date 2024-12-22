package info.mapes.weekend.repositories

import com.mongodb.client.model.Filters.text
import com.mongodb.client.model.ReplaceOptions
import info.mapes.weekend.entities.MovieWatchingEntity
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.ne

@Singleton
class MovieWatchingRepository(
    private val movieCollection: CoroutineCollection<MovieWatchingEntity>,
) {
    suspend fun getCountOfWatchedMovies(): Long {
        return movieCollection.countDocuments(
            MovieWatchingEntity::finished ne null,
        )
    }

    suspend fun getCurrentlyWatchingMovie(): MovieWatchingEntity? {
        return movieCollection.findOne(
            MovieWatchingEntity::finished eq null,
        )
    }

    suspend fun getMovieByTmdbId(tmdbId: Int): MovieWatchingEntity? {
        return movieCollection.findOne(
            MovieWatchingEntity::tmdbId eq tmdbId,
        )
    }

    suspend fun saveMovie(movie: MovieWatchingEntity): Boolean {
        return movieCollection.replaceOne(
            and(
                MovieWatchingEntity::tmdbId eq movie.tmdbId,
            ),
            movie,
            ReplaceOptions().upsert(true),
        ).wasAcknowledged()
    }

    fun searchForWatchedMovie(searchText: String): Flow<MovieWatchingEntity> {
        return movieCollection
            .find(text(searchText))
            .sort(MovieWatchingEntity::year eq 1)
            .toFlow()
    }
}
