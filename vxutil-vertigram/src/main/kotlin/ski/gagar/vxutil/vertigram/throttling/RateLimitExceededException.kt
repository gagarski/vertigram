package ski.gagar.vxutil.vertigram.throttling

import ski.gagar.vxutil.jackson.BadRequest
import java.time.Duration

class RateLimitExceededException(msg: String? = null, cause: Throwable? = null, val retryIn: Duration?) :
    Exception(msg, cause), BadRequest
