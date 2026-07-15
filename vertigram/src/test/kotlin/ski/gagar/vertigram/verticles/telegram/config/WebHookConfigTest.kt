package ski.gagar.vertigram.verticles.telegram.config

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class WebHookConfigTest {
    @Test
    fun `accepts Telegram-compatible secret token`() {
        assertDoesNotThrow {
            WebHookConfig(secretToken = "Stable_webhook-secret-123")
        }
    }

    @Test
    fun `rejects unsupported secret token characters`() {
        assertThrows(IllegalArgumentException::class.java) {
            WebHookConfig(secretToken = "bot:token")
        }
    }

    @Test
    fun `allows random fallback when secret token is omitted`() {
        assertDoesNotThrow {
            WebHookConfig(secretToken = null)
        }
    }
}
