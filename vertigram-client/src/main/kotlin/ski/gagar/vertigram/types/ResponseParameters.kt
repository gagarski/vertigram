package ski.gagar.vertigram.types

import java.time.Duration

data class ResponseParameters(
    val migrateToChatId: Long? = null,
    val retryAfter: Duration? = null
)
