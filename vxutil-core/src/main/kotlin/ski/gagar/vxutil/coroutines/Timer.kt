package ski.gagar.vxutil.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.setTimer(
    delay: Duration,
    context: CoroutineContext = this.coroutineContext,
    task: suspend () -> Unit) = launch {
    delay(delay.toMillis())
    withContext(context) {
        task()
    }
}

fun CoroutineScope.setTimerNonCancellable(
    delay: Duration,
    task: suspend () -> Unit
) = setTimer(delay, NonCancellable, task)
