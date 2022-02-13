package the.weekend.bot.services

import discord4j.common.util.Snowflake
import discord4j.core.DiscordClient
import discord4j.core.`object`.presence.ClientActivity
import discord4j.core.`object`.presence.ClientPresence.online
import discord4j.core.event.domain.lifecycle.DisconnectEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.discordjson.json.EmbedData
import discord4j.discordjson.json.MessageCreateRequest
import io.micronaut.context.annotation.Context
import org.slf4j.LoggerFactory
import the.weekend.bot.configs.DiscordClientConfiguration
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Context
class DiscordEventService(
    private val discordClientConfiguration: DiscordClientConfiguration
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val rootClient = DiscordClient.create(discordClientConfiguration.token)
    private val client = rootClient.gateway().login().block()!!

    @PostConstruct
    fun handleStartup() {
        logger.info("Discord Client Created with configured Channels : ${discordClientConfiguration.channels}")

        client.on(ReadyEvent::class.java).subscribe(::handleReady)
        client.on(DisconnectEvent::class.java).subscribe(::handleDisconnect)
    }

    @PreDestroy
    fun handlePreDestroy() {
        client.logout().block()
        client.onDisconnect().block()
        logger.info("Logged out and disconnected from Discord")
    }

    fun sendMessage(message: String) {
        discordClientConfiguration.channels
            .filter { it.enabled }
            .forEach {
                logger.info("Sending to ${it.name} : Message: \'$message\'")
                client.rest().getChannelById(it.id).createMessage(message.trimStart()).block()
            }
    }

    fun sendEmbedMessage(message: String, embedData: EmbedData, channelId: Long) {
        val messageCreateRequest = MessageCreateRequest.builder().content(message).embed(embedData).build()
        val channelSnowflake = Snowflake.of(channelId)
        client.rest().getChannelById(channelSnowflake).createMessage(messageCreateRequest).block()
    }

    fun setStatus(movieText: String) {
        client.updatePresence(
            online(ClientActivity.watching(movieText))
        ).block()
    }

    private fun handleReady(event: ReadyEvent) {
        logger.info("Logged in as ${event.self.username}#${event.self.discriminator}")
    }

    private fun handleDisconnect(event: DisconnectEvent) {
        logger.info("Disconnected from Discord! '$event'")
    }
}
