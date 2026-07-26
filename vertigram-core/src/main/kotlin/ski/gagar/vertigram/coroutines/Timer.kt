package ski.gagar.vertigram.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toKotlinDuration

/**
 * Run [task] after [delay]
 *
 * @param delay The delay
 * @param context Coroutine context
 * @param task The task to run
 */
fun CoroutineScope.setTimer(
    delay: Duration,
    context: CoroutineContext = this.coroutineContext,
    task: suspend () -> Unit
) = launch {
    delay(delay.toKotlinDuration())
    withContext(context) {
        task()
    }
}

/**
 * [setTimer] on [NonCancellable]
 *
 * @param delay The delay
 * @param task The task to run
 *
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
