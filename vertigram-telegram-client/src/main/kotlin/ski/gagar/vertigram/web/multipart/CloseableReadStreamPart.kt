package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import ski.gagar.vertigram.util.io.CloseableReadStream
import ski.gagar.vertigram.util.io.ReadStreamWrapper

/**
 * A multipart file backed by a lazily supplied [CloseableReadStream].
 *
 * [streamProvider] is invoked when transmission reaches this part. When [owned] is `true`, the stream is closed after
 * successful transmission, failure, or cancellation; when it is `false`, the provider's caller retains ownership.
 * [contentType] defaults to `application/octet-stream`.
 */
class CloseableReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> CloseableReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val owned: Boolean = true
) : Part() {
    override val contentDisposition = formDataContentDisposition(name, filename)

    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )
    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        if (owned)
            ReadStreamWrapper.ofCloseable(streamProvider)
        else
            ReadStreamWrapper.ofNonCloseable(streamProvider)
}
