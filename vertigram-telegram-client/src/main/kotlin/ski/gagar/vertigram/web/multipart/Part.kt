package ski.gagar.vertigram.web.multipart

import io.netty.handler.codec.http.HttpHeaderNames
import io.vertx.core.buffer.Buffer
import io.vertx.core.streams.ReadStream
import kotlinx.coroutines.CoroutineScope
import ski.gagar.vertigram.util.io.ConcatStream
import ski.gagar.vertigram.util.io.CloseableReadStream
import ski.gagar.vertigram.util.io.ReadStreamWrapper

typealias ReadStreamWrapperBuffer = ReadStreamWrapper<Buffer, ReadStream<Buffer>>


/**
 * A single low-level `multipart/form-data` part.
 *
 * This is also the public transport SPI used by custom Telegram attachments. [contentDisposition] is the complete
 * `Content-Disposition` header value and [headers] contains any additional part headers. Custom implementations are
 * responsible for producing valid, injection-safe header values; the built-in part classes perform the required
 * quoting and validation.
 *
 * Data is acquired lazily through [dataStreamWrapper] when transmission begins. The wrapper defines whether and how
 * its underlying stream is closed. A part should be treated as a single-transmission object.
 */
abstract class Part {
    /** Complete value of this part's `Content-Disposition` header. */
    abstract val contentDisposition: String

    /** Additional multipart headers, excluding `Content-Disposition`. */
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

    /**
     * Opens this part's data stream and defines its cleanup behavior.
     *
     * Implementations should defer resource acquisition until this method is called.
     */
    protected abstract suspend fun dataStreamWrapper(): ReadStreamWrapperBuffer

    private suspend fun getAndAcquireDataStream(): ReadStreamWrapperBuffer {
        val streamWrapper = dataStreamWrapper()
        this.streamWrapper = streamWrapper
        return streamWrapper
    }

    suspend fun stream(scope: CoroutineScope): CloseableReadStream<Buffer> =
        scope.ConcatStream(
            ReadStreamWrapper.ofNonCloseable(headersBuffer.asSingletonStream()),
            getAndAcquireDataStream(),
            ReadStreamWrapper.ofNonCloseable(NL.asSingletonStream()),
        )

    companion object
}

internal fun formDataContentDisposition(name: String, filename: String? = null): String = buildString {
    append("form-data; name=")
    appendQuotedContentDispositionValue("name", name)
    if (filename != null) {
        append("; filename=")
        appendQuotedContentDispositionValue("filename", filename)
    }
}

private fun StringBuilder.appendQuotedContentDispositionValue(parameter: String, value: String) {
    require(value.none(Char::isISOControl)) {
        "Multipart Content-Disposition $parameter contains a control character"
    }

    append('"')
    value.forEach { char ->
        if (char == '"' || char == '\\') append('\\')
        append(char)
    }
    append('"')
}
