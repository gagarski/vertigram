package ski.gagar.vxutil.vertigram.throttling

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ski.gagar.vxutil.jackson.BadRequest
import java.time.Duration

@JsonIgnoreProperties("message", "suppressed", "localizedMessage")
class RateLimitExceededException(msg: String? = null, cause: Throwable? = null, val retryIn: Duration?) :
    Exception(msg, cause), BadRequest
