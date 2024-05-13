package the.weekend.bot.configs

import discord4j.common.util.Snowflake
import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import jakarta.inject.Singleton

@Singleton
@Context
@ConfigurationProperties("discord")
data class DiscordClientConfiguration
    @ConfigurationInject
    constructor(
        val token: String,
        val rawBotId: Long,
        val schedule: String,
        val channels: Set<ChannelConfiguration>,
    ) {
        val botId: Snowflake = Snowflake.of(rawBotId)
    }

data class ChannelConfiguration(
    val name: String,
    val rawId: Long,
    val enabled: Boolean,
) {
    val id: Snowflake = Snowflake.of(rawId)
}
