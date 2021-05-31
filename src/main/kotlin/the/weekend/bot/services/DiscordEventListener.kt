package the.weekend.bot.services

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import io.micronaut.context.annotation.Context
import org.slf4j.LoggerFactory
import the.weekend.bot.configs.DiscordClientConfiguration
import javax.annotation.PostConstruct
import javax.inject.Singleton
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.lifecycle.DisconnectEvent

import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.MessageEvent
import discord4j.discordjson.json.MessageCreateRequest
import discord4j.discordjson.json.gateway.MessageCreate
import io.micronaut.scheduling.annotation.Scheduled
import javax.annotation.PreDestroy


@Singleton
@Context
class DiscordEventListener(
    private val discordClientConfiguration: DiscordClientConfiguration
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val rootClient = DiscordClient.create(discordClientConfiguration.token)
    private val client = rootClient.gateway().login().block()!!

    @PostConstruct
    fun handleStartup() {
        logger.info("Discord Client Create with token ${discordClientConfiguration.token.take(5)}...")
        logger.info("With Channels : ${discordClientConfiguration.channels}")

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

    private fun handleMessageCreate(event: MessageCreateEvent) {
        logger.info("Message Received from '${event.member.get().displayName}' : '${event.message.content}'")
    }

    private fun handleReady(event: ReadyEvent) {
        logger.info("Logged in as ${event.self.username}#${event.self.discriminator}")
    }

    private fun handleDisconnect(event: DisconnectEvent) {
        logger.info("Disconnected from Discord! '$event'")
    }

    @Scheduled(cron = "0 0 19 * * FRI")
    internal fun sendIt() {
        logger.info("sendIt Triggered")
        discordClientConfiguration.channels
            .filter { it.enabled }
            .forEach {
                logger.info("Sending to ${it.name}")
                client.rest().getChannelById(it.id).createMessage(MESSAGE_CONTENT.trimStart()).block()
            }
    }

    companion object {
        private const val THE_WEEKEND_LINK = "https://www.youtube.com/watch?v=V_cnK8Cd6Ag"
        const val MESSAGE_CONTENT = "**It's 7pm Somewhere**\n$THE_WEEKEND_LINK"
    }
}
