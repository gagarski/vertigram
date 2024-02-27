package ski.gagar.vertigram.services.logback

import ski.gagar.vertigram.Vertigram
import ski.gagar.vertigram.logback.attachEventBusLogging
import ski.gagar.vertigram.services.VertigramInitializer

class VertigramLogbackInitializer : VertigramInitializer {
    override fun Vertigram.initialize() {
        attachEventBusLogging()
    }
}