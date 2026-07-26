package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import ski.gagar.vertigram.util.io.ReadStreamWrapper

/**
 * A multipart file backed by a lazily supplied Vert.x [ReadStream].
 *
 * [streamProvider] is invoked when transmission reaches this part. [closer] is then invoked exactly once when the
 * acquired stream is released after successful transmission, failure, or cancellation. By default it does nothing,
 * leaving resource ownership with the caller. [contentType] defaults to `application/octet-stream`.
 */
class ReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> ReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val closer: (suspend (stream: ReadStream<Buffer>) -> Unit) = {}
) : Part() {
    override val contentDisposition = formDataContentDisposition(name, filename)


    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )

    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        ReadStreamWrapper.of(streamProvider, closer)
}
