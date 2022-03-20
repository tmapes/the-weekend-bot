package the.weekend.bot.services

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService,
    private val movieWatchingService: MovieWatchingService
) {

    @Scheduled(fixedRate = "30s", initialDelay = "10s")
    fun updateStatus() = runBlocking {
        movieWatchingService.getOrStartMovie()?.let {
            discordEventService.setStatus(
                it.toDiscordText()
            )
        }
    }
}
