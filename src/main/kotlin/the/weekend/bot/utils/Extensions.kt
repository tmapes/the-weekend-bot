package the.weekend.bot.utils

inline fun <T, R> T?.letOrElse(nonNull: (T) -> R, ifNull: () -> Unit) {
    if (this != null) nonNull(this) else ifNull()
}
