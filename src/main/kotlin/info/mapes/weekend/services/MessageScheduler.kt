package info.mapes.weekend.services

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MessageScheduler(
    private val discordEventService: DiscordEventService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "\${discord.schedule}")
    fun sendIt() {
        logger.info("sendIt Triggered")
        discordEventService.sendMessage(MESSAGE_CONTENT)
    }

    companion object {
        private const val THE_WEEKEND_LINK = "https://www.youtube.com/watch?v=V_cnK8Cd6Ag"
        const val MESSAGE_CONTENT = "**It's 7pm Somewhere**\n$THE_WEEKEND_LINK"
    }
}
