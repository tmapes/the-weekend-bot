package the.weekend.bot.services

import kotlin.coroutines.Continuation
import spock.lang.Specification
import the.weekend.bot.domains.Movie

import java.time.Duration
import java.time.Instant

class StatusSchedulerSpec extends Specification {

    def discordEventServiceMock = Mock(DiscordEventService)
    def movieWatchingServiceMock = Mock(MovieWatchingService)
    def statusScheduler = new StatusScheduler(discordEventServiceMock, movieWatchingServiceMock)

    def "status updated when movie found"() {
        given:
        def movie = new Movie("Title", 2020, Duration.ZERO, Instant.EPOCH, 1234)

        when:
        statusScheduler.updateStatus()

        then:
        1 * movieWatchingServiceMock.getOrStartMovie(false, _ as Continuation) >> movie
        1 * discordEventServiceMock.setStatus(movie.toDiscordText())

        0 * _
    }

    def "np status change when no movie"() {
        when:
        statusScheduler.updateStatus()

        then:
        1 * movieWatchingServiceMock.getOrStartMovie(false, _ as Continuation) >> null

        0 * _
    }
}
