package ski.gagar.vxutil.io

import io.vertx.core.Handler
import io.vertx.core.streams.ReadStream

class SingletonStream<T>(private val value: T) : ReadStream<T> {
    enum class State {
        NOT_FIRED,
        FIRED
    }
    private var paused = false
    private var state = State.NOT_FIRED
    private var handler: Handler<T>? = null
    private var exceptionHandler: Handler<Throwable>? = null
    private var endHandler: Handler<Void?>? = null

    private inline fun doCatching(block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {
            exceptionHandler?.handle(t)
        }
    }

    private fun fire() {
        doCatching { handler?.handle(value) }
        doCatching { endHandler?.handle(null) }
        state = State.FIRED
    }

    override fun pause(): ReadStream<T> = apply {
        paused = true
    }

    override fun resume(): ReadStream<T> = apply {
        paused = false
        if (state != State.NOT_FIRED) return@apply
        fire()
    }

    override fun handler(handler: Handler<T>?): ReadStream<T> = apply {
        this.handler = handler
        if (state != State.NOT_FIRED) return@apply
        if (paused) return@apply
        fire()
    }

    override fun fetch(amount: Long): ReadStream<T> = apply {
        resume()
    }

    override fun exceptionHandler(handler: Handler<Throwable>?): ReadStream<T> = apply {
        this.exceptionHandler = handler
    }

    override fun endHandler(endHandler: Handler<Void?>?): ReadStream<T> = apply {
        this.endHandler = endHandler
    }

    override fun toString(): String {
        return "SingletonStream(value=$value)"
    }


}
