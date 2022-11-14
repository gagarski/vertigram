package ski.gagar.vxutil.verticles.children.messages

enum class DeathReason {
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT
}

data class DeathNotice(val id: String, val reason: DeathReason)
