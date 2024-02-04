package ski.gagar.vertigram.verticles.children.messages

enum class DeathReason {
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}

data class DeathNotice(val id: String, val reason: DeathReason)
