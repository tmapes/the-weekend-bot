package the.weekend.bot.services

import discord4j.core.DiscordClient
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.lifecycle.DisconnectEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.MessageDeleteEvent
import discord4j.core.event.domain.message.MessageUpdateEvent
import discord4j.core.event.domain.message.ReactionAddEvent
import discord4j.core.event.domain.message.ReactionRemoveEvent
import io.micronaut.context.annotation.Context
import org.slf4j.LoggerFactory
import the.weekend.bot.configs.DiscordClientConfiguration
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Singleton


@Singleton
@Context
class DiscordEventService(
    private val discordClientConfiguration: DiscordClientConfiguration
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val rootClient = DiscordClient.create(discordClientConfiguration.token)
    private val client = rootClient.gateway().login().block()!!

    @PostConstruct
    fun handleStartup() {
        logger.info("Discord Client Create with token ${discordClientConfiguration.token.take(5)}...")
        logger.info("With Channels : ${discordClientConfiguration.channels}")
        logger.info("Configured with Cron String '${discordClientConfiguration.schedule}'")

        client.on(ReadyEvent::class.java).subscribe(::handleReady)
        client.on(DisconnectEvent::class.java).subscribe(::handleDisconnect)

        // Ignore these events
        client.on(MessageCreateEvent::class.java).subscribe(::ignoreEvent)
        client.on(MessageDeleteEvent::class.java).subscribe(::ignoreEvent)
        client.on(MessageUpdateEvent::class.java).subscribe(::ignoreEvent)
        client.on(ReactionAddEvent::class.java).subscribe(::ignoreEvent)
        client.on(ReactionRemoveEvent::class.java).subscribe(::ignoreEvent)

        // Debug Print any other events
        client.on(Event::class.java).subscribe(::handleGenericEvent)
    }

    @PreDestroy
    fun handlePreDestroy() {
        client.logout().block()
        client.onDisconnect().block()
    }

    fun sendMessage(message: String) {
        discordClientConfiguration.channels
            .filter { it.enabled }
            .forEach {
                logger.info("Sending to ${it.name}")
                client.rest().getChannelById(it.id).createMessage(message.trimStart()).block()
            }
    }

    fun setStatus(newStatus: String) {
        client.updatePresence(
            Presence.online(
                Activity.playing(newStatus)
            )
        ).block()
    }

    private fun ignoreEvent(ignored: Event) {
        // Do nothing with $ignored
    }

    private fun handleGenericEvent(event: Event) {
        logger.debug("Unmapped event '${event::class.simpleName}' received from Discord : $event")
    }

    private fun handleReady(event: ReadyEvent) {
        logger.info("Logged in as ${event.self.username}#${event.self.discriminator}")
    }

    private fun handleDisconnect(event: DisconnectEvent) {
        logger.info("Disconnected from Discord! '$event'")
    }

}
