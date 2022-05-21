package ski.gagar.vxutil.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import ski.gagar.vxutil.io.CloseableReadStream

class CloseableReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> CloseableReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val dataLength: Long? = null,
    streamOwned: Boolean = true
) : Part<CloseableReadStream<Buffer>>(streamOwned) {
    override val contentDisposition =
        """form-data; name=$name; filename=$filename"""

    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )
    override suspend fun dataLength(): Long? = dataLength

    override suspend fun dataStream(): CloseableReadStream<Buffer> = streamProvider()

    override suspend fun close(stream: CloseableReadStream<Buffer>) {
        stream.close()
    }
}
