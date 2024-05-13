package info.mapes.weekend.factories

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.event.ApplicationShutdownEvent
import jakarta.inject.Singleton
import org.litote.kmongo.coroutine.CoroutineClient

@Singleton
class MongoEventListener(
    private val mongoClient: CoroutineClient,
) : ApplicationEventListener<ApplicationShutdownEvent> {
    override fun onApplicationEvent(event: ApplicationShutdownEvent?) = mongoClient.close()
}
