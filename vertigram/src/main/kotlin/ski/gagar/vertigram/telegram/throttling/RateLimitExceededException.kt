package ski.gagar.vertigram.telegram.throttling

import ski.gagar.vertigram.util.exceptions.VertigramException
import java.time.Duration

/**
 * An exception thrown by [ThrottlingTelegram] when the rate limit is exceeded.
 */
class RateLimitExceededException(
    msg: String? = null,
    cause: Throwable? = null,
    /**
     * Duration when the request can be retried
     */
    val retryIn: Duration?
) :
    VertigramException(msg, cause)
