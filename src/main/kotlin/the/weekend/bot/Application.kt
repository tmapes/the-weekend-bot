package the.weekend.bot

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("the.weekend.bot")
		.banner(false)
		.start()
}
