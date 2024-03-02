package ski.gagar.vertigram.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext

/**
 * Run [task] after [delay]
 */
fun CoroutineScope.setTimer(
    /**
     * The delay
     */
    delay: Duration,
    /**
     * Coroutine context
     */
    context: CoroutineContext = this.coroutineContext,
    /**
     * The task to run
     */
    task: suspend () -> Unit
) = launch {
    delay(delay.toMillis())
    withContext(context) {
        task()
    }
}

/**
 * [setTimer] on [NonCancellable]
 */
fun CoroutineScope.setTimerNonCancellable(
    /**
     * The delay
     */
    delay: Duration,
    /**
     * The task to run
     */
    task: suspend () -> Unit
) = setTimer(delay, NonCancellable, task)
