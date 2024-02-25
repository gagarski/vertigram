package ski.gagar.vertigram.eventbus.messages

data class Request<T>(
    val payload: T
)