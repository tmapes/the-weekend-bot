package the.weekend.bot.services

import io.micronaut.context.annotation.Value
import the.weekend.bot.domains.Movie
import javax.inject.Singleton

@Singleton
class MovingPostingService(
    private val discordEventService: DiscordEventService,
    @Value("\${discord.movie-channel-id}") private val movieChannelId: Long
) {
    fun postWatchedMovie(movie: Movie) {
        val messageText = MOVIE_FINISHED.format(movie.title, movie.year)
        discordEventService.sendMessage(messageText, movieChannelId)
    }

    companion object {
        const val MOVIE_FINISHED = "Just finished '%s' (%d)"
    }
}
