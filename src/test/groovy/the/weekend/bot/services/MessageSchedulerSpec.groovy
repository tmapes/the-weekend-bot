package the.weekend.bot.services

import spock.lang.Specification

class MessageSchedulerSpec extends Specification {

    def discordEventServiceMock = Mock(DiscordEventService)
    def messageScheduler = new MessageScheduler(discordEventServiceMock)

    def "sendIt works as expected"() {
        when:
        messageScheduler.sendIt()

        then:
        1 * discordEventServiceMock.sendMessage(MessageScheduler.MESSAGE_CONTENT)

        0 * _
    }
}
