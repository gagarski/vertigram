package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CoroutineScope
import ski.gagar.vertigram.io.ConcatStream
import ski.gagar.vertigram.io.ReadStreamWrapper

typealias ReadStreamWrapperBuffer = ReadStreamWrapper<Buffer, ReadStream<Buffer>>
abstract class Part {
    abstract val contentDisposition: String
    open val headers = linkedMapOf<String, String>()
    private var streamWrapper: ReadStreamWrapperBuffer? = null

    private val headersBuffer: Buffer by lazy {
        val buf = Buffer.buffer()
        buf.appendString("${HttpHeaderNames.CONTENT_DISPOSITION}: $contentDisposition$NL")
        for ((k, v) in headers) {
            buf.appendString("$k: $v$NL")
        }
        buf.appendString(NL)
    }

    private fun headersLength() = headersBuffer.length().toLong()

    protected abstract suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer

    private suspend fun getAndAcquireDataStream(): ReadStreamWrapperBuffer {
        val streamWrapper = dataStreamWrapper()
        this.streamWrapper = streamWrapper
        return streamWrapper
    }

    protected open suspend fun dataLength(): Long? = null

    private fun trailingLength() = NL.length.toLong()

    suspend fun stream(scope: CoroutineScope): ReadStream<Buffer> =
        scope.ConcatStream(
            ReadStreamWrapper.ofNonCloseable(headersBuffer.asSingletonStream()),
            getAndAcquireDataStream(),
            ReadStreamWrapper.ofNonCloseable(NL.asSingletonStream()),
        )
    suspend fun length() = dataLength()?.let {
        headersLength() + it + trailingLength()
    }

    companion object
}
