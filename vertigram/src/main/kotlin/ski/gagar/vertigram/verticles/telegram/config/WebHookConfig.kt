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
     * Reverse proxy configuration
     */
    val proxy: Proxy? = null
) : UpdateReceiverConfig {
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
}
