package the.weekend.bot

import io.micronaut.runtime.Micronaut.build
import java.util.TimeZone

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    build()
        .args(*args)
        .packages("the.weekend.bot")
        .banner(false)
        .start()
}
