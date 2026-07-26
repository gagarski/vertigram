package ski.gagar.vertigram.util.io

import io.vertx.core.file.AsyncFile
import io.vertx.core.streams.ReadStream

/**
 * A lazy wrapper for [ReadStream]
 *
 * @param provider Provider of the stream which will be called on [open]
 * @param closer Closer which will be called on [close]
 */
class ReadStreamWrapper<T, out S : ReadStream<T>>(
    private val provider: suspend () -> S,
    private val closer: suspend (stream: S) -> Unit = { }
) {
    private var stream: S? = null
    private var closed = false

    suspend fun open(): S {
        check(stream == null && !closed) {
            "Stream wrapper can only be opened once"
        }
        return provider().also {
            stream = it
        }
    }

    fun get(): S {
        return requireNotNull(stream) {
            "Stream should be opened"
        }
    }

    suspend fun close() {
        if (closed) return
        val stream = stream ?: return
        closed = true
        closer(stream)
    }

    companion object {
        fun ofFile(f: AsyncFile) = ReadStreamWrapper({ f }) { it.close() }
        fun ofFile(fP: suspend () -> AsyncFile) = ReadStreamWrapper(fP) { it.close() }
        fun <T> ofNonCloseable(s: ReadStream<T>) = ReadStreamWrapper({ s })
        fun <T> ofNonCloseable(sP: suspend () -> ReadStream<T>) = ReadStreamWrapper(sP)
        fun <T> ofCloseable(s: CloseableReadStream<T>) = ReadStreamWrapper({ s }) { it.close() }
        fun <T> ofCloseable(sP: suspend () -> CloseableReadStream<T>) = ReadStreamWrapper(sP) { it.close() }
        fun <T> of(s: ReadStream<T>, closer: suspend (ReadStream<T>) -> Unit) = ReadStreamWrapper({ s }, closer)
        fun <T> of(sP: suspend () -> ReadStream<T>, closer: suspend (ReadStream<T>) -> Unit) = ReadStreamWrapper(sP, closer)
    }
}
