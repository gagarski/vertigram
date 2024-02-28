package ski.gagar.vertigram.verticles.telegram.config

sealed interface UpdateReceiverConfig {
    val type: Type

    enum class Type {
        LONG_POLL,
        WEB_HOOK
    }
}