package ski.gagar.vxutil

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.setTimer(d: Duration,
                            context: CoroutineContext = NonCancellable,
                            handler: suspend () -> Unit) = launch {
    delay(d.toMillis())
    withContext(context) {
        handler()
    }
}
