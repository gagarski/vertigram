package ski.gagar.vertigram.verticles.telegram.config

data class WebHookConfig(
    val port: Int = 8008,
    val host: String = "localhost",
    val base: String = "",
    val publicUrl: String = "https://$host:$port/$base",
    val proxy: Proxy? = null
) {
    data class Proxy(
        val trustDomainSockets: Boolean = false,
        val trustedNetworks: Set<String> = setOf()
    )
}
