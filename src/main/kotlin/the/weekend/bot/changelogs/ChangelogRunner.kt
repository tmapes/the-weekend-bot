package the.weekend.bot.changelogs

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver
import io.mongock.runner.standalone.MongockStandalone
import the.weekend.bot.configs.MongoConfiguration
import javax.inject.Singleton

@Singleton
class ChangelogRunner(
    private val mongoConfiguration: MongoConfiguration,
    private val mongoClient: MongoClient,
    private val mongoDatabase: MongoDatabase
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
            .setTransactionEnabled(false)
            .buildRunner()

        mongockRunner.execute()
    }
}
