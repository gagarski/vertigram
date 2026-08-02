package ski.gagar.vertigram.telegram.client.impl

import io.vertx.core.Vertx
import io.vertx.core.impl.NoStackTraceTimeoutException
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.methods.GetMe
import java.time.Duration

class TelegramTimeoutTest {
    @Test
    fun `regular call uses short poll timeout`() = runBlocking {
        val vertx = Vertx.vertx()
        val server = vertx.createHttpServer()
            .requestHandler {
                // Keep the response open until the client times out.
            }
            .listen(0)
            .coAwait()
        val telegram = TelegramImpl(
            token = "token",
            vertx = vertx,
            options = TelegramImplOptions(
                tgBase = "http://127.0.0.1:${server.actualPort()}",
                shortPollTimeout = Duration.ofMillis(50),
                longPollTimeout = Duration.ofSeconds(5)
            )
        )

        try {
            val failure = withTimeout(1_000) {
                runCatching {
                    telegram.call(
                        telegram.mapper.typeFactory.constructType(User.Me::class.java),
                        GetMe
                    )
                }.exceptionOrNull()
            }

            assertTrue(failure is NoStackTraceTimeoutException)
        } finally {
            telegram.close()
            server.close().coAwait()
            vertx.close().coAwait()
        }
    }
}
