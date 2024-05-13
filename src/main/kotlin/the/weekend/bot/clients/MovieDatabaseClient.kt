package the.weekend.bot.clients

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import the.weekend.bot.models.TmdbMovie
import the.weekend.bot.models.TmdbMovieDiscoveryResponse

@Client("the-movie-database")
interface MovieDatabaseClient {
    @Get("/3/movie/top_rated")
    suspend fun topRatedMovies(
        @QueryValue("page") page: Int,
        @QueryValue("language") language: String = "en-US",
        @QueryValue("region") region: String = "US",
    ): TmdbMovieDiscoveryResponse

    @Get("/3/movie/{movie_id}")
    suspend fun getMovieById(
        @PathVariable("movie_id") movieId: Int,
        @QueryValue("append_to_response") appendToResponse: Set<String> = setOf("credits", "images"),
    ): HttpResponse<TmdbMovie>
}
