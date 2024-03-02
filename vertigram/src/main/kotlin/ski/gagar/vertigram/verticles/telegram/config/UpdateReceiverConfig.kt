package ski.gagar.vertigram.verticles.telegram.config

/**
 * Common interface for [LongPollerConfig] and [WebHookConfig]
 */
sealed interface UpdateReceiverConfig {
    val type: Type

    enum class Type {
        LONG_POLL,
        WEB_HOOK
    }
}