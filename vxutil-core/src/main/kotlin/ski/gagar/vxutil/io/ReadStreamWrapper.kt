package ski.gagar.vxutil.io

import io.vertx.core.file.AsyncFile
import io.vertx.core.streams.ReadStream

class ReadStreamWrapper<T, out S : ReadStream<T>>(
    private val provider: suspend () -> S,
    private val closer: suspend (stream: S) -> Unit = { }
) {
    private lateinit var stream: S

    suspend fun open(): S {
        stream = provider()
        return stream
    }

    fun get(): S {
        require(this::stream.isInitialized) {
            "Stream should be opened"
        }
        return stream
    }

    suspend fun close() {
        require(this::stream.isInitialized) {
            "Stream should be opened"
        }
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
