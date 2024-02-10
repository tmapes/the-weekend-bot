package the.weekend.bot.factories

import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import spock.lang.Ignore
import spock.lang.Specification
import the.weekend.bot.configs.MongoConfiguration
import the.weekend.bot.entities.MovieWatchingEntity

@Ignore
class MongoFactorySpec extends Specification {

    private def mongoClientMock = Mock(CoroutineClient)
    private def mongoDatabaseMock = Mock(CoroutineDatabase)
    private def mongoCollectionMock = Mock(CoroutineDatabase)
    private def mongoConfigurationMock = GroovyMock(MongoConfiguration)

    private static def MONGO_URI = "mongodb://127.0.1.1/database"
    private static def DATABASE_NAME = "database"
    private static def COLLECTION_NAME = "collection"

    def "mongoClient() returns a MongoClient"() {
        when:
        def database = new MongoFactory().mongoClient(mongoConfigurationMock)

        then:
        database instanceof CoroutineClient

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
