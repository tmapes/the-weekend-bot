package info.mapes.weekend.services

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService,
    private val movieWatchingService: MovieWatchingService,
) {
    @Scheduled(fixedRate = "1m", initialDelay = "1m")
    fun updateStatus() =
        runBlocking {
            val currentMovie = movieWatchingService.getOrStartMovie()
            if (currentMovie != null) {
                discordEventService.setStatus(currentMovie.toDiscordText(), true)
            } else {
                discordEventService.setStatus(SEARCHING_STATUS_TEXT, false)
            }
        }

    companion object {
        const val SEARCHING_STATUS_TEXT = "Searching for a Movie"
    }
}
