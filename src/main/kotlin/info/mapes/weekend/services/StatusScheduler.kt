package info.mapes.weekend.services

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService,
    private val movieWatchingService: MovieWatchingService,
) {
    @Volatile
    private var lastStatus: String? = null

    @Scheduled(fixedRate = "1m", initialDelay = "1m")
    fun updateStatus() =
        runBlocking {
            val currentMovie = movieWatchingService.getOrStartMovie()
            val (nextStatus, watching) = (
                if (currentMovie != null) {
                    currentMovie.toDiscordText() to true
                } else {
                    SEARCHING_STATUS_TEXT to false
                }
            )

            if (nextStatus == lastStatus) {
                return@runBlocking
            }
            lastStatus = nextStatus
            discordEventService.setStatus(nextStatus, watching)
        }

    companion object {
        const val SEARCHING_STATUS_TEXT = "Searching for a Movie"
    }
}
