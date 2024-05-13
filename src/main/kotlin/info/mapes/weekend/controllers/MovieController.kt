package info.mapes.weekend.controllers

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import info.mapes.weekend.domains.Movie
import info.mapes.weekend.services.MovieWatchingService

@Controller
class MovieController(
    private val movieWatchingService: MovieWatchingService,
) {
    @Put("/start_movie")
    @Status(HttpStatus.ACCEPTED)
    suspend fun startMovie(): Movie? {
        return movieWatchingService.getOrStartMovie(true)
    }

    @Get
    suspend fun getCurrentMovie(): Movie? {
        return movieWatchingService.getOrStartMovie(false)
    }
}
