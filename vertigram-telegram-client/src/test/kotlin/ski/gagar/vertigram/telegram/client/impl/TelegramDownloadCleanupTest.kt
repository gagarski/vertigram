package ski.gagar.vertigram.telegram.client.impl

import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.nio.file.Files

class TelegramDownloadCleanupTest {
    @Test
    fun `removes output file when download fails`() = runBlocking {
        val vertx = Vertx.vertx()
        val output = Files.createTempFile("vertigram-download-", ".tmp")
        val telegram = TelegramImpl(
            token = "token",
            vertx = vertx,
            options = TelegramImplOptions(tgBase = "http://[invalid")
        )

        try {
            val failure = runCatching {
                telegram.downloadFile("file", output.toString())
            }.exceptionOrNull()

            assertNotNull(failure)
            assertFalse(Files.exists(output))
        } finally {
            telegram.close()
            vertx.close().coAwait()
            Files.deleteIfExists(output)
        }
    }

    @Test
    fun `removes output file when download is cancelled`() = runBlocking {
        val vertx = Vertx.vertx()
        val requestReceived = CompletableDeferred<Unit>()
        val server = vertx.createHttpServer()
            .requestHandler {
                requestReceived.complete(Unit)
            }
            .listen(0)
            .coAwait()
        val output = Files.createTempFile("vertigram-download-", ".tmp")
        val telegram = TelegramImpl(
            token = "token",
            vertx = vertx,
            options = TelegramImplOptions(tgBase = "http://127.0.0.1:${server.actualPort()}")
        )

        try {
            val download = launch {
                telegram.downloadFile("file", output.toString())
            }
            withTimeout(5_000) {
                requestReceived.await()
            }

            download.cancelAndJoin()

            assertFalse(Files.exists(output))
        } finally {
            telegram.close()
            server.close().coAwait()
            vertx.close().coAwait()
            Files.deleteIfExists(output)
        }
    }
}
