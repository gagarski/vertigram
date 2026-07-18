package ski.gagar.vertigram.services.logback

import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.logback.attachEventBusLoggingIfConfigured
import ski.gagar.vertigram.services.VertigramInitializer

/**
 * Attaches configured Logback event-bus appenders to a [Vertigram] instance.
 */
class VertigramLogbackInitializer : VertigramInitializer {
    override fun Vertigram.initialize() {
        if (isLogbackAvailable()) {
            attachEventBusLoggingIfConfigured()
        }
    }

    private fun isLogbackAvailable(): Boolean =
        runCatching {
            Class.forName(
                "ch.qos.logback.classic.LoggerContext",
                false,
                VertigramLogbackInitializer::class.java.classLoader
            )
        }.isSuccess
}
