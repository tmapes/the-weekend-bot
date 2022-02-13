package the.weekend.bot.controllers

import kotlin.coroutines.Continuation
import spock.lang.Specification
import the.weekend.bot.services.MovieWatchingService

import static the.weekend.bot.utils.TestUtilsKt.runBlocking

class MovieControllerSpec extends Specification {

    def movieWatchingServiceMock = Mock(MovieWatchingService)
    def movieController = new MovieController(movieWatchingServiceMock)

    def "startMovie"() {
        when:
        def output = runBlocking { movieController.startMovie(it) }

        then:
        !output
        1 * movieWatchingServiceMock.getOrStartMovie(true, _ as Continuation) >> null

        0 * _
    }

    def "getCurrentMovie"() {
        when:
        def output = runBlocking { movieController.getCurrentMovie(it) }

        then:
        !output
        1 * movieWatchingServiceMock.getOrStartMovie(false, _ as Continuation) >> null

        0 * _
    }
}
