package ski.gagar.vxutil.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream

class ReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> ReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val dataLength: Long? = null,
    private val closer: (suspend (stream: ReadStream<Buffer>) -> Unit)? = null
) : Part<ReadStream<Buffer>>(null != closer) {
    override val contentDisposition =
        """form-data; name=$name; filename=$filename"""


    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )
    override suspend fun dataLength(): Long? = dataLength

    override suspend fun dataStream(): ReadStream<Buffer> = streamProvider()

    override suspend fun close(stream: ReadStream<Buffer>) {
        closer?.invoke(stream)
    }
}
