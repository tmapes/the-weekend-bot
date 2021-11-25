package the.weekend.bot.configs

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.EachProperty
import java.time.Duration

@EachProperty("movies", list = true)
data class MovieConfiguration @ConfigurationInject constructor(
    val title: String,
    val year: Int,
    val length: Duration
)
