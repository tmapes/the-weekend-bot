package the.weekend.bot.utils

import discord4j.core.`object`.entity.channel.TextChannel
import discord4j.core.event.domain.message.MessageBulkDeleteEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import discord4j.core.event.domain.message.MessageDeleteEvent
import discord4j.core.event.domain.message.MessageEvent
import discord4j.core.event.domain.message.MessageUpdateEvent

fun MessageEvent.getChannelName(): String {
    return when (this) {
        is MessageCreateEvent -> (this.message.channel.block() as TextChannel).name
        is MessageUpdateEvent -> (this.channel.block() as TextChannel).name
        is MessageDeleteEvent -> (this.channel.block() as TextChannel).name
        is MessageBulkDeleteEvent -> (this.channel.block() as TextChannel).name
        else -> "unknown (${this::class.simpleName})"
    }
}