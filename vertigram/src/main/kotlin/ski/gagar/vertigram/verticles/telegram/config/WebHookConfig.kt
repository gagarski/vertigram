package ski.gagar.vertigram.verticles.telegram.config

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Configuration of the web-hook itself.
 *
 * @see ski.gagar.vertigram.verticles.telegram.WebHook
 * @see ski.gagar.vertigram.verticles.telegram.WebHook.Config
 * @see ski.gagar.vertigram.verticles.telegram.ensemble.deployTelegramEnsemble
 */
data class WebHookConfig(
    /**
     * Port to listen
     */
    val port: Int = 8008,
    /**
     * Host to listen
     */
    val host: String = "localhost",
    /**
     * URL base path
     */
    val base: String = "",
    /**
     * Base public URL, i.e. the one under each the web-hook is visible for telegram servers
     *
     * Useful if your bot is behind reverse HTTP proxy
     */
    val publicUrl: String = "https://$host:$port/$base",
    /**
     * Secret sent by Telegram in the `X-Telegram-Bot-Api-Secret-Token` header.
     *
     * Explicitly configuring an independently generated, stable value is strongly recommended. Use 1 to 256 characters
     * from `A-Z`, `a-z`, `0-9`, `_`, and `-`. Do not reuse the Telegram bot token or derive this value by replacing
     * characters in the bot token.
     *
     * When `null`, [ski.gagar.vertigram.verticles.telegram.WebHook] generates a random secret for the current deployment
     * and logs a warning. The generated value is neither exposed nor persisted and therefore changes on every restart.
     * This fallback is intended for local development only and is unsuitable for rolling or multi-instance deployments.
     */
    val secretToken: String? = null,
    /**
     * Reverse proxy configuration
     */
    val proxy: Proxy? = null
) : UpdateReceiverConfig {
    init {
        require(secretToken == null || SECRET_TOKEN_REGEX.matches(secretToken)) {
            "Webhook secret token must contain 1 to 256 characters from A-Z, a-z, 0-9, _ and -"
        }
    }

    /**
     * Reverse proxy configuration (value for [proxy])
     */
    data class Proxy(
        /**
         * Whether to trust IP address headers (`X-Real-IP` or `X-Forwarded-For`) for domain sockets when logging
         *
         * @see [ski.gagar.vertigram.web.server.RealIpLoggerHandler]
         */
        val trustDomainSockets: Boolean = false,
        /**
         * Network addresses whose IP address headers (`X-Real-IP` or `X-Forwarded-For`) are trusted for domain sockets when logging
         *
         * @see [ski.gagar.vertigram.web.server.RealIpLoggerHandler]
         */
        val trustedNetworks: Set<String> = setOf()
    )

    @JsonIgnore
    override val type: UpdateReceiverConfig.Type = UpdateReceiverConfig.Type.WEB_HOOK

    companion object {
        private val SECRET_TOKEN_REGEX = Regex("^[A-Za-z0-9_-]{1,256}$")
    }
}
