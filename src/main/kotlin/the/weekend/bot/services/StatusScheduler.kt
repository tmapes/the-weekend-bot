package the.weekend.bot.services

import io.micronaut.scheduling.annotation.Scheduled
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Singleton

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService
) {

    @Scheduled(initialDelay = "5s", fixedRate = "10s")
    fun updateStatus() {
        // there has to be a better way to do this but i can't be asked right now
        // TODO get better at date manipulation
        val now = LocalDateTime.now()
        val nextFriday = now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
        val nextFridayAtSeven = with(nextFriday) {
            this.withHour(19)
                .withSecond(0)
                .withMinute(0)
                .withNano(0)
        }

        val secondsUntil = now.until(nextFridayAtSeven, ChronoUnit.SECONDS)
        val statusText = NEXT_WEEKEND_TEXT.format(secondsUntil)

        discordEventService.setStatus(statusText)
    }

    companion object {
        const val NEXT_WEEKEND_TEXT = "nothing for %d seconds"
    }
}