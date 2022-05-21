package ski.gagar.vxutil.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.file.AsyncFile
import io.vertx.kotlin.coroutines.await

class FilePart(name: String,
               filename: String,
               val fileProvider: suspend () -> AsyncFile,
               contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
               fileOwned: Boolean = true
) : Part<AsyncFile>(fileOwned) {
    override val contentDisposition =
        """form-data; name=$name; filename=$filename"""

    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )

    override suspend fun dataLength(): Long {
        val f = fileProvider()

        try {
            return f.size().await()
        } finally {
            f.close().await()
        }
    }

    override suspend fun dataStream(): AsyncFile = fileProvider()

    override suspend fun close(stream: AsyncFile) {
        stream.close().await()
    }
}
