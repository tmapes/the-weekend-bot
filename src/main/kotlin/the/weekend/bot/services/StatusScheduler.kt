package the.weekend.bot.services

import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Singleton

@Singleton
class StatusScheduler(
    private val discordEventService: DiscordEventService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRate = "30s")
    fun updateStatus() {
        val now = LocalDateTime.now(CENTRAL_ZONE)
        val fridayToCountTo = useToday(now)

        val nextFridayAtSeven = with(fridayToCountTo) {
            this.withHour(19)
                .withSecond(0)
                .withMinute(0)
                .withNano(0)
        }

        logger.debug("The time is '$now', we're going to count down to '$nextFridayAtSeven'")

        val minutesUntil = NEXT_WEEKEND_TEXT.format(now.until(nextFridayAtSeven, ChronoUnit.MINUTES))
        discordEventService.setStatus(minutesUntil)
    }

    private fun useToday(now: LocalDateTime): LocalDateTime {
        return if (now.dayOfWeek == DayOfWeek.FRIDAY && now.hour < 19)
            now
        else
            now.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
    }

    companion object {
        const val NEXT_WEEKEND_TEXT = "CS:GO for %d minutes"
        val CENTRAL_ZONE: ZoneId = ZoneId.of("America/Chicago")
    }
}
