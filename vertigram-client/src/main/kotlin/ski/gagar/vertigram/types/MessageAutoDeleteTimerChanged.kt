package ski.gagar.vertigram.types

import java.time.Duration

data class MessageAutoDeleteTimerChanged(
    val messageAutoDeleteTime: Duration
)
