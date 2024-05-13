package info.mapes.weekend.utils

import discord4j.core.event.domain.message.MessageCreateEvent
import info.mapes.weekend.utils.Constants.WATCHED_PATTERN
import io.micronaut.core.util.StringUtils

inline fun <T, R> T?.letOrElse(
    nonNull: (T) -> R,
    ifNull: () -> Unit,
) {
    if (this != null) nonNull(this) else ifNull()
}

fun MessageCreateEvent.getWatchedQuery(): String {
    val matcher = WATCHED_PATTERN.find(message.content)
    matcher?.let {
        return it.groups[1]!!.value
    }

    return StringUtils.EMPTY_STRING
}
