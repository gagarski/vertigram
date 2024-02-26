package ski.gagar.vertigram.telegram.throttling

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ski.gagar.vertigram.eventbus.exceptions.VertigramException
import java.time.Duration

class RateLimitExceededException(msg: String? = null, cause: Throwable? = null, val retryIn: Duration?) :
    VertigramException(msg, cause)
