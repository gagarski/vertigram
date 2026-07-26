package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.file.AsyncFile
import ski.gagar.vertigram.util.io.ReadStreamWrapper

/**
 * A multipart file backed by a lazily supplied Vert.x [AsyncFile].
 *
 * [fileProvider] is invoked when transmission reaches this part. When [owned] is `true`, the file is closed after
 * successful transmission, failure, or cancellation; when it is `false`, the provider's caller retains ownership
 * and must close it. [contentType] defaults to `application/octet-stream`.
 */
class FilePart(name: String,
               filename: String,
               val fileProvider: suspend () -> AsyncFile,
               contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
               private val owned: Boolean = true
) : Part() {
    override val contentDisposition = formDataContentDisposition(name, filename)

    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )

    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        if (owned) ReadStreamWrapper.ofFile(fileProvider) else ReadStreamWrapper.ofNonCloseable(fileProvider)
}
