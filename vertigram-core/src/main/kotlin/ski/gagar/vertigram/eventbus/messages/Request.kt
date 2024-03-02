package ski.gagar.vertigram.eventbus.messages

/**
 * Request wrapper for [ski.gagar.vertigram.Vertigram.EventBus]
 */
data class Request<T>(
    val payload: T
)