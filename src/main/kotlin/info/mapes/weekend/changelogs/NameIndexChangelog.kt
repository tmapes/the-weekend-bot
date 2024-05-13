package info.mapes.weekend.changelogs

import com.mongodb.client.model.IndexOptions
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineCollection
import info.mapes.weekend.entities.MovieWatchingEntity
import javax.inject.Named

@ChangeUnit(id = "name-text-index", order = "2", author = "tmapes")
class NameIndexChangelog(
    @Named("movieCollection") private val movieCollection: CoroutineCollection<MovieWatchingEntity>,
) {
    @Execution
    fun execute(): Unit =
        runBlocking(Dispatchers.IO) {
            movieCollection.createIndex(
                Document(mapOf("name" to "text")),
                IndexOptions().background(false),
            )
        }

    @RollbackExecution
    fun rollback(): Unit =
        runBlocking(Dispatchers.IO) {
            movieCollection.dropIndex(
                Document(mapOf("name" to "text")),
            )
        }
}
