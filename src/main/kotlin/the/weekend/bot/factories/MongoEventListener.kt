package the.weekend.bot.factories

import com.mongodb.client.MongoClient
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.event.ApplicationShutdownEvent
import jakarta.inject.Singleton

@Singleton
class MongoEventListener(
    private val mongoClient: MongoClient
) : ApplicationEventListener<ApplicationShutdownEvent> {

    override fun onApplicationEvent(event: ApplicationShutdownEvent?) = mongoClient.close()
}
