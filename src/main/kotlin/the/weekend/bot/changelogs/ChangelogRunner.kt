package the.weekend.bot.changelogs

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver
import io.mongock.runner.standalone.MongockStandalone
import jakarta.inject.Singleton
import the.weekend.bot.configs.MongoConfiguration
import the.weekend.bot.entities.MovieWatchingEntity

@Singleton
class ChangelogRunner(
    private val mongoConfiguration: MongoConfiguration,
    private val mongoClient: MongoClient,
    private val mongoDatabase: MongoDatabase,
    private val movieCollection: MongoCollection<MovieWatchingEntity>
) : ApplicationEventListener<ServerStartupEvent> {

    override fun onApplicationEvent(event: ServerStartupEvent) {
        val mongockRunner = MongockStandalone.builder()
            .setDriver(
                MongoSync4Driver.withDefaultLock(
                    mongoClient,
                    mongoConfiguration.databaseName
                )
            )
            .addMigrationScanPackage("the.weekend.bot.changelogs")
            .addDependency("mongoConfiguration", mongoConfiguration)
            .addDependency("mongoDatabase", mongoDatabase)
            .addDependency("movieCollection", movieCollection)
            .setTransactionEnabled(false)
            .buildRunner()

        mongockRunner.execute()
    }
}
