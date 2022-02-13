package the.weekend.bot.utils

import kotlinx.coroutines.runBlocking
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun <T> runBlocking(lambda: (Continuation<T>) -> T): T = runBlocking {
    suspendCoroutine<T> { continuation: Continuation<T> ->
        try {
            continuation.resume(lambda(continuation))
        } catch (throwable: Throwable) {
            continuation.resumeWithException(throwable)
        }
    }
}
