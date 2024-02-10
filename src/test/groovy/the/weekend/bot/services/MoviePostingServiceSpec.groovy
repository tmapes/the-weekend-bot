package the.weekend.bot.services

import spock.lang.Specification
import the.weekend.bot.clients.MovieDatabaseClient

class MoviePostingServiceSpec extends Specification {

    def discordEventServiceMock = Mock(DiscordEventService)
    def movieDatabaseClientMock = Mock(MovieDatabaseClient)
    def movieChannelIdMock = 1234l
    def moviePostingService = new MoviePostingService(discordEventServiceMock, movieDatabaseClientMock, movieChannelIdMock)
}
