package the.weekend.bot.changelogs

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.IndexOptions
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import org.bson.Document
import the.weekend.bot.entities.MovieWatchingEntity
import javax.inject.Named

@ChangeUnit(id = "name-text-index", order = "2", author = "tmapes")
class NameIndexChangelog(
    @Named("movieCollection") private val movieCollection: MongoCollection<MovieWatchingEntity>
) {

    @Execution
    fun execute() {
        movieCollection.createIndex(
            Document(mapOf("name" to "text")),
            IndexOptions().background(false)
        )
    }

    @RollbackExecution
    fun rollback() {
        movieCollection.dropIndex(
            Document(mapOf("name" to "text")),
        )
    }
}
