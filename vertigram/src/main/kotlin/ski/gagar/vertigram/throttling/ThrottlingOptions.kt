package ski.gagar.vertigram.throttling

import java.time.Duration

data class ThrottlingOptions(
    val chatBurstPerSecond: Int? = 3,
    val globalPerSecond: Int? = 30,
    val chatPerMinute: Int? = 20,
    val maxWait: Duration = Duration.ofSeconds(10)
)
