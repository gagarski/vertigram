package ski.gagar.vertigram.telegram.exceptions

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.types.ResponseParameters
import ski.gagar.vertigram.telegram.types.methods.GetManagedBotToken
import java.time.Duration

class TelegramCallExceptionTest {
    @Test
    fun `exposes Telegram response parameters`() {
        val responseParameters = ResponseParameters(
            migrateToChatId = 42,
            retryAfter = Duration.ofSeconds(10)
        )
        val request = GetManagedBotToken(userId = 1)

        val exception = TelegramCallException.create(
            status = 429,
            ok = false,
            description = "Too Many Requests",
            call = request,
            responseHeaders = emptyMap(),
            responseParameters = responseParameters
        )

        assertSame(responseParameters, exception.responseParameters)
    }
}
