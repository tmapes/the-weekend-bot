package the.weekend.bot.services

import io.micronaut.scheduling.annotation.Scheduled
import javax.inject.Singleton

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService,
    private val movieWatchingService: MovieWatchingService
) {

    @Scheduled(fixedRate = "30s")
    suspend fun updateStatus() {
        movieWatchingService.getOrStartMovie()?.let {
            discordEventService.setStatus(
                it.toDiscordText()
            )
        }
    }
}
