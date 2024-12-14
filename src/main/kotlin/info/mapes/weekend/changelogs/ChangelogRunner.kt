package info.mapes.weekend.changelogs

import info.mapes.weekend.configs.MongoConfiguration
import info.mapes.weekend.entities.MovieWatchingEntity
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver
import io.mongock.runner.standalone.MongockStandalone
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

@Singleton
class ChangelogRunner(
    private val mongoConfiguration: MongoConfiguration,
    private val mongoClient: CoroutineClient,
    private val mongoDatabase: CoroutineDatabase,
    private val movieCollection: CoroutineCollection<MovieWatchingEntity>,
) : ApplicationEventListener<ServerStartupEvent> {
    override fun onApplicationEvent(event: ServerStartupEvent) {
        val mongockRunner =
            MongockStandalone.builder()
                .setDriver(
                    MongoReactiveDriver.withDefaultLock(
                        mongoClient.client,
                        mongoConfiguration.databaseName,
                    ),
                )
                .addMigrationScanPackage("info.mapes.weekend.changelogs")
                .addDependency("mongoConfiguration", mongoConfiguration)
                .addDependency("mongoDatabase", mongoDatabase)
                .addDependency("movieCollection", movieCollection)
                .setTransactional(false)
                .buildRunner()

        mongockRunner.execute()
    }
}
