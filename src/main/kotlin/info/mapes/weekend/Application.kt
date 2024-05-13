package info.mapes.weekend

import io.micronaut.runtime.Micronaut.build
import java.util.TimeZone

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    build()
        .args(*args)
        .packages("info.mapes.weekend")
        .banner(false)
        .start()
}
