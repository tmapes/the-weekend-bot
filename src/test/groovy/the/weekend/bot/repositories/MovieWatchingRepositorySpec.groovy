package the.weekend.bot.repositories

import com.mongodb.client.model.Filters
import org.bson.BsonNull
import org.bson.conversions.Bson
import org.litote.kmongo.coroutine.CoroutineCollection
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll
import the.weekend.bot.entities.MovieWatchingEntity

import java.time.Instant

@Ignore
class MovieWatchingRepositorySpec extends Specification {
    def movieCollectionMock = Mock(CoroutineCollection)
    def movieWatchingRepository = new MovieWatchingRepository(movieCollectionMock)

    def "getCountOfWatchedMovies works correctly"() {
        when:
        def output = movieWatchingRepository.getCountOfWatchedMovies()

        then:
        1l == output
        1 * movieCollectionMock.countDocuments(_) >> {Bson filter ->
            filter = filter.toBsonDocument()
            assert filter.size() == 1
            assert filter["finished"].asDocument()["\$ne"] instanceof BsonNull

            return 1
        }

        0 * _
    }

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

    def "searchForWatchedMovie works as expected"() {
        given:
        def searchTerm = "text"
        def findIterableMock = Mock(FindIterable)

        when:
        def output = movieWatchingRepository.searchForWatchedMovie(searchTerm)

        then:
        1 * movieCollectionMock.find(_) >> { Bson filter ->
            assert filter instanceof Filters.TextFilter
            searchTerm == filter.search

            return findIterableMock
        }
        output

        0  * _
    }
}
