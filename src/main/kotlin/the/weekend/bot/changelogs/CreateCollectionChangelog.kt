package the.weekend.bot.changelogs

import com.mongodb.client.MongoDatabase
import io.mongock.api.annotations.ChangeUnit
import io.mongock.api.annotations.Execution
import io.mongock.api.annotations.RollbackExecution
import the.weekend.bot.configs.MongoConfiguration
import javax.inject.Named

@ChangeUnit(id = "collection-init", order = "1", author = "tmapes")
class CollectionChangeUnit(
    @Named("mongoDatabase") private val mongoDatabase: MongoDatabase,
    @Named("mongoConfiguration") private val mongoConfiguration: MongoConfiguration
) {

    @Execution
    fun execute() {
        mongoDatabase.createCollection(mongoConfiguration.collectionName)
    }

    @RollbackExecution
    fun rollback() {
        mongoDatabase.getCollection(mongoConfiguration.collectionName).drop()
    }
}
