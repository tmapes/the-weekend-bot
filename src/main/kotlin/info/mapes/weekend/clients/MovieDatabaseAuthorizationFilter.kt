package info.mapes.weekend.clients

import io.micronaut.context.annotation.Value
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@Filter(serviceId = ["the-movie-database"])
class MovieDatabaseAuthorizationFilter(
    @Value("\${tmdb.api-token}") private val tmdbApiToken: String,
) : HttpClientFilter {
    override fun doFilter(
        request: MutableHttpRequest<*>,
        chain: ClientFilterChain,
    ): Publisher<out HttpResponse<*>> = chain.proceed(request.bearerAuth(tmdbApiToken))
}
