package the.weekend.bot.clients

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import the.weekend.bot.models.TmdbMovie
import the.weekend.bot.models.TmdbMovieDiscoveryResponse
import java.util.concurrent.CompletableFuture

@Client("https://api.themoviedb.org", configuration = MovieDatabaseClientConfiguration::class)
interface MovieDatabaseClient {

    @Get("/3/movie/top_rated")
    fun topRatedMovies(
        @QueryValue("api_key") apiKey: String,
        @QueryValue("page") page: Int,
        @QueryValue("language") language: String = "en-US",
        @QueryValue("region") region: String = "US",
    ): CompletableFuture<HttpResponse<TmdbMovieDiscoveryResponse>>

    @Get("/3/movie/{movie_id}")
    fun getMovieById(
        @PathVariable("movie_id") movieId: Int,
        @QueryValue("api_key") apiKey: String
    ): TmdbMovie?
}
