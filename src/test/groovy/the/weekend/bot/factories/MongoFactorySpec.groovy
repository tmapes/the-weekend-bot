package the.weekend.bot.factories

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import spock.lang.Specification
import the.weekend.bot.configs.MongoConfiguration
import the.weekend.bot.entities.MovieWatchingEntity

class MongoFactorySpec extends Specification {

    private def mongoClientMock = Mock(MongoClient)
    private def mongoDatabaseMock = Mock(MongoDatabase)
    private def mongoCollectionMock = Mock(MongoCollection)
    private def mongoConfigurationMock = GroovyMock(MongoConfiguration)

    private static def MONGO_URI = "mongodb://127.0.1.1/database"
    private static def DATABASE_NAME = "database"
    private static def COLLECTION_NAME = "collection"

    def "mongoClient() returns a MongoClient"() {
        when:
        def database = new MongoFactory().mongoClient(mongoConfigurationMock)

        then:
        database instanceof MongoClient

        1 * mongoConfigurationMock.getUri() >> MONGO_URI
        0 * _
    }

    def "mongoDatabase() returns a MongoDatabase"() {
        when:
        def database = new MongoFactory().mongoDatabase(mongoConfigurationMock, mongoClientMock)

        then:
        mongoDatabaseMock == database

        1 * mongoConfigurationMock.getDatabaseName() >> DATABASE_NAME
        1 * mongoClientMock.getDatabase(DATABASE_NAME) >> mongoDatabaseMock
        0 * _
    }

    def "movieCollection() returns a MongoCollection"() {
        when:
        def collection = new MongoFactory().movieCollection(mongoConfigurationMock, mongoDatabaseMock)

        then:
        mongoCollectionMock == collection

        1 * mongoConfigurationMock.getCollectionName() >> COLLECTION_NAME
        1 * mongoDatabaseMock.getCollection(COLLECTION_NAME, MovieWatchingEntity.class) >> mongoCollectionMock
        0 * _
    }

}
