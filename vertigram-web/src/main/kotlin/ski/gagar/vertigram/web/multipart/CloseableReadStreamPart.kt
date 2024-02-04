package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.buffer.Buffer
import ski.gagar.vertigram.io.CloseableReadStream
import ski.gagar.vertigram.io.ReadStreamWrapper

class CloseableReadStreamPart(
    name: String,
    filename: String,
    private val streamProvider: suspend () -> CloseableReadStream<Buffer>,
    contentType: String = HttpHeaderValues.APPLICATION_OCTET_STREAM.toString(),
    private val dataLength: Long? = null,
    private val owned: Boolean = true
) : Part() {
    override val contentDisposition =
        """form-data; name=$name; filename=$filename"""

    override val headers = linkedMapOf(
        HttpHeaderNames.CONTENT_TYPE.toString() to contentType
    )
    override suspend fun dataLength(): Long? = dataLength

    override suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer =
        if (owned)
            ReadStreamWrapper.ofCloseable(streamProvider)
        else
            ReadStreamWrapper.ofNonCloseable(streamProvider)
}
