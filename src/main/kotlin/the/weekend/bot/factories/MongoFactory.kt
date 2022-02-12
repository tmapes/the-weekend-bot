package the.weekend.bot.factories

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.micronaut.context.annotation.Factory
import org.litote.kmongo.KMongo
import the.weekend.bot.configs.MongoConfiguration
import the.weekend.bot.entities.MovieWatchingEntity
import javax.inject.Singleton

@Factory
class MongoFactory {

    @Singleton
    fun mongoClient(mongoConfiguration: MongoConfiguration): MongoClient {
        return KMongo.createClient(mongoConfiguration.uri)
    }

    @Singleton
    fun mongoDatabase(mongoConfiguration: MongoConfiguration, mongoClient: MongoClient): MongoDatabase {
        return mongoClient.getDatabase(mongoConfiguration.databaseName)
    }

    @Singleton
    fun movieCollection(
        mongoConfiguration: MongoConfiguration,
        mongoDatabase: MongoDatabase
    ): MongoCollection<MovieWatchingEntity> {
        return mongoDatabase.getCollection(mongoConfiguration.collectionName, MovieWatchingEntity::class.java)
    }
}
