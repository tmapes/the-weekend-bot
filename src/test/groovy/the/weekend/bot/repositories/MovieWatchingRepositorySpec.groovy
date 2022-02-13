package the.weekend.bot.repositories

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import org.bson.BsonNull
import org.bson.conversions.Bson
import spock.lang.Specification
import spock.lang.Unroll
import the.weekend.bot.entities.MovieWatchingEntity

import java.time.Instant

class MovieWatchingRepositorySpec extends Specification {
    def movieCollectionMock = Mock(MongoCollection)
    def movieWatchingRepository = new MovieWatchingRepository(movieCollectionMock)

    @Unroll
    def "getCurrentlyWatchingMovie works correctly - #scenario"() {
        given:
        def findIterableMock = Mock(FindIterable)

        when:
        def output = movieWatchingRepository.getCurrentlyWatchingMovie()

        then:
        expected == output
        1 * movieCollectionMock.find(_) >> { Bson filter ->
            filter = filter.toBsonDocument()
            assert filter.size() == 1
            assert filter.toBsonDocument()["finished"] instanceof BsonNull

            return findIterableMock
        }

        1 * findIterableMock.first() >> expected

        0 * _

        where:
        scenario         || expected
        "non null value" || new MovieWatchingEntity("name", 2020, 1, 1, Instant.EPOCH, Instant.MAX)
        "null value"     || null
    }

    @Unroll
    def "getMovieByNameAndYear works correctly - #scenario"() {
        given:
        def findIterableMock = Mock(FindIterable)
        def name = "Movie"
        def year = 1970i

        when:
        def output = movieWatchingRepository.getMovieByNameAndYear(name, year)

        then:
        expected == output
        1 * movieCollectionMock.find(_) >> { Bson filter ->
            filter = filter.toBsonDocument()
            assert filter.size() == 1

            def andFilter = filter["\$and"].asArray()
            assert andFilter.size() == 2
            assert name == andFilter[0].asDocument()["name"].asString().value
            assert year == andFilter[1].asDocument()["year"].asInt32().value
            return findIterableMock
        }

        1 * findIterableMock.first() >> expected

        0 * _

        where:
        scenario         || expected
        "non null value" || new MovieWatchingEntity("name", 2020, 1, 1, Instant.EPOCH, Instant.MAX)
        "null value"     || null
    }
}
