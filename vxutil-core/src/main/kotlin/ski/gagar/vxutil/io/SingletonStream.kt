package ski.gagar.vxutil.io

import io.vertx.core.Handler
import io.vertx.core.streams.ReadStream

class SingletonStream<T>(private val value: T) : ReadStream<T> {
    private var paused = false
    private var state = State.NOT_FIRED
    private var handler: Handler<T>? = null
    private var exceptionHandler: Handler<Throwable>? = null
    private var endHandler: Handler<Void?>? = null

    override fun pause(): ReadStream<T> = apply {
        paused = true
    }

    override fun resume(): ReadStream<T> = apply {
        paused = false
        fireValueIfNecessary()
        fireEndIfNecessary()
    }

    override fun handler(handler: Handler<T>?): ReadStream<T> = apply {
        this.handler = handler
        fireValueIfNecessary()
        fireEndIfNecessary()
    }

    override fun fetch(amount: Long): ReadStream<T> = apply {
        if (amount != 0L) {
            resume()
        }
    }

    override fun exceptionHandler(handler: Handler<Throwable>?): ReadStream<T> = apply {
        this.exceptionHandler = handler
    }

    override fun endHandler(endHandler: Handler<Void?>?): ReadStream<T> = apply {
        this.endHandler = endHandler
        fireEndIfNecessary()
    }

    private fun fireValueIfNecessary() {
        val h = handler
        if (paused) return
        if (state != State.NOT_FIRED) return
        if (h == null) return
        state = State.FIRED
        h.handle(value)
    }

    private fun fireEndIfNecessary() {
        val h = endHandler
        if (paused) return
        if (state != State.FIRED) return
        if (h == null) return
        state = State.DONE
        h.handle(null)
    }

    override fun toString(): String {
        return "SingletonStream(value=$value)"
    }

    private enum class State {
        NOT_FIRED,
        FIRED,
        DONE
    }
}
