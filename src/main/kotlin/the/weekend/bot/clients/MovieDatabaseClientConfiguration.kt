package the.weekend.bot.clients

import io.micronaut.http.client.HttpClientConfiguration
import io.micronaut.runtime.ApplicationConfiguration
import javax.inject.Singleton

@Singleton
class MovieDatabaseClientConfiguration(
    private val poolSettings: ConnectionPoolConfiguration,
    applicationConfiguration: ApplicationConfiguration
) : HttpClientConfiguration(applicationConfiguration) {

    override fun isExceptionOnErrorStatus(): Boolean {
        return false
    }

    override fun getConnectionPoolConfiguration(): ConnectionPoolConfiguration {
        return poolSettings
    }

}
