package the.weekend.bot.changelogs

import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineDatabase
import the.weekend.bot.configs.MongoConfiguration
import javax.inject.Named

@ChangeUnit(id = "collection-init", order = "1", author = "tmapes")
class CollectionChangeUnit(
    @Named("mongoDatabase") private val mongoDatabase: CoroutineDatabase,
    @Named("mongoConfiguration") private val mongoConfiguration: MongoConfiguration
) {

    @Execution
    fun execute() = runBlocking(Dispatchers.IO) {
        mongoDatabase.createCollection(mongoConfiguration.collectionName)
    }

    @RollbackExecution
    fun rollback() = runBlocking(Dispatchers.IO) {
        mongoDatabase.getCollection<Document>(mongoConfiguration.collectionName).drop()
    }
}
