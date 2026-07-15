package ski.gagar.vertigram.telegram.client.impl

import io.vertx.core.impl.NoStackTraceTimeoutException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TelegramTransportRedactionTest {
    @Test
    fun `redacts token from timeout exception`() {
        val token = "123456:secret-token"
        val original = NoStackTraceTimeoutException(
            "Timed out while executing POST /bot$token/sendMessage"
        )

        val redacted = original.redactTelegramBotToken(token)

        assertTrue(redacted.message.orEmpty().contains("<redacted-bot-token>"))
        assertFalse(redacted.stackTraceToString().contains(token))
    }
}
