package the.weekend.bot.services

import discord4j.core.DiscordClient
import discord4j.core.event.domain.lifecycle.DisconnectEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
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

        client.on(MessageCreateEvent::class.java)
            .filter {
                it.member.get().id != discordClientConfiguration.botId
            }
            .subscribe(::handleMessageCreate)
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

    private fun handleMessageCreate(event: MessageCreateEvent) {
        logger.info("Message Received from '${event.member.get().displayName}' : '${event.message.content}'")
    }

    private fun handleReady(event: ReadyEvent) {
        logger.info("Logged in as ${event.self.username}#${event.self.discriminator}")
    }

    private fun handleDisconnect(event: DisconnectEvent) {
        logger.info("Disconnected from Discord! '$event'")
    }

}
