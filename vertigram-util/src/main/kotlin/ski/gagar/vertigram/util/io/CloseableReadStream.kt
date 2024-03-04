package ski.gagar.vertigram.util.io

import io.vertx.core.streams.ReadStream

/**
 * [ReadStream] which supports [close] (suspend) operation
 */
interface CloseableReadStream<T> : ReadStream<T> {
    suspend fun close()
}
