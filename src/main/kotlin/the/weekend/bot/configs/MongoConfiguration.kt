package the.weekend.bot.configs

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context

@Context
@ConfigurationProperties("mongodb")
data class MongoConfiguration @ConfigurationInject constructor(
    val uri: String,
    val databaseName: String,
    val collectionName: String,
)
