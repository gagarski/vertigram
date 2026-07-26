package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.file.OpenOptions
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.coAwait
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files

object MultipartChunkedTransferTest {
    @Test
    fun `multipart should be chunked and open a file only for streaming`() = runBlocking {
        val vertx = Vertx.vertx()
        val requestHeaders = CompletableDeferred<MultiMap>()
        val file = Files.createTempFile("vertigram-multipart-", ".txt")
        Files.writeString(file, "multipart payload")
        var openCount = 0

        val server = vertx.createHttpServer()
            .requestHandler { request ->
                requestHeaders.complete(request.headers())
                request.body()
                    .onSuccess { request.response().end() }
                    .onFailure { request.response().setStatusCode(500).end() }
            }
            .listen(0)
            .coAwait()
        val client = WebClient.create(vertx)

        try {
            val form = MultipartForm(
                FilePart(
                    name = "document",
                    filename = file.fileName.toString(),
                    fileProvider = {
                        openCount++
                        vertx.fileSystem().open(file.toString(), OpenOptions()).coAwait()
                    }
                )
            )

            form.send(client.post(server.actualPort(), "127.0.0.1", "/"))

            val headers = withTimeout(5_000) { requestHeaders.await() }
            assertNull(headers[HttpHeaderNames.CONTENT_LENGTH])
            assertEquals(
                HttpHeaderValues.CHUNKED.toString(),
                headers[HttpHeaderNames.TRANSFER_ENCODING]
            )
            assertTrue(
                headers[HttpHeaderNames.CONTENT_TYPE]
                    .startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())
            )
            assertEquals(1, openCount)
        } finally {
            client.close()
            server.close().coAwait()
            vertx.close().coAwait()
            Files.deleteIfExists(file)
        }
    }
}
