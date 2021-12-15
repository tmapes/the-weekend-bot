package the.weekend.bot.clients

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import the.weekend.bot.models.TmdbMovie
import the.weekend.bot.models.TmdbMovieDiscoveryResponse

@Client("https://api.themoviedb.org")
interface MovieDatabaseClient {

    @Get("/3/discover/movie")
    fun discoverMovie(
        @QueryValue("api_key") apiKey: String,
        @QueryValue("page") page: Int,
        @QueryValue("language") language: String = "en-US",
        @QueryValue("region") region: String = "US",
        @QueryValue("sort_by") sortBy: String = "popularity.asc",
        @QueryValue("release_date.gte") releaseDateGte: String = "1980-01-01"
    ): TmdbMovieDiscoveryResponse?

    @Get("/3/movie/{movie_id}")
    fun getMovieById(
        @PathVariable("movie_id") movieId: Int,
        @QueryValue("api_key") apiKey: String
    ): TmdbMovie?
}
