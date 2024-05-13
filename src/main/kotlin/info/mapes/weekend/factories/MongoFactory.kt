package info.mapes.weekend.factories

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import info.mapes.weekend.configs.MongoConfiguration
import info.mapes.weekend.entities.MovieWatchingEntity

@Factory
class MongoFactory {
    @Singleton
    fun mongoClient(mongoConfiguration: MongoConfiguration): CoroutineClient {
        return KMongo.createClient(mongoConfiguration.uri).coroutine
    }

    @Singleton
    fun mongoDatabase(
        mongoConfiguration: MongoConfiguration,
        mongoClient: CoroutineClient,
    ): CoroutineDatabase {
        return mongoClient.getDatabase(mongoConfiguration.databaseName)
    }

    @Singleton
    fun movieCollection(
        mongoConfiguration: MongoConfiguration,
        mongoDatabase: CoroutineDatabase,
    ): CoroutineCollection<MovieWatchingEntity> {
        return mongoDatabase.getCollection<MovieWatchingEntity>(mongoConfiguration.collectionName)
    }
}
