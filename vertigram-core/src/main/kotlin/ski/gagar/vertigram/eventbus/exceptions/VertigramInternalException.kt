package ski.gagar.vertigram.eventbus.exceptions

open class VertigramInternalException : VertigramException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}