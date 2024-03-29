package ski.gagar.vertigram.util

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.WorkerExecutor
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Callable
import java.util.concurrent.Delayed
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * Create [kotlinx.coroutines.CoroutineDispatcher] from [workerExecutor]
 */
fun Vertx.workerExecutorDispatcher(workerExecutor: WorkerExecutor) =
    WorkerExecutorService(workerExecutor, this).asCoroutineDispatcher()


class WorkerExecutorServiceError(msg: String) : Error(msg)

private class WorkerExecutorServiceScheduledFuture(
    val vertx: Vertx,
    val workerExecutorService: WorkerExecutorService,
    val task: Runnable,
    val delay: Long,
    val unit: TimeUnit) : ScheduledFuture<Any>, Handler<Long> {

    // pending : null (no completion)
    // done : true
    // cancelled : false
    val completion = AtomicReference<Boolean?>()
    var id: Long? = null

    fun schedule() {
        id = vertx.setTimer(unit.toMillis(delay), this)
    }

    override fun get(): Any? {
        return null
    }

    override fun get(timeout: Long, unit: TimeUnit): Any? {
        return null
    }

    override fun isCancelled(): Boolean {
        return completion.get() == false
    }

    override fun handle(event: Long?) {
        if (completion.compareAndSet(null, true)) {
            workerExecutorService.execute(task)
        }
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        return if (completion.compareAndSet(null, false)) {
            vertx.cancelTimer(id!!)
        } else {
            false
        }
    }

    override fun isDone(): Boolean {
        return completion.get() == true
    }

    override fun getDelay(u: TimeUnit): Long {
        return u.convert(unit.toNanos(delay), TimeUnit.NANOSECONDS)
    }

    override fun compareTo(other: Delayed): Int {
        return getDelay(TimeUnit.NANOSECONDS).compareTo(other.getDelay(TimeUnit.NANOSECONDS))
    }
}

private class WorkerExecutorService(
    private val workerExecutor: WorkerExecutor,
    private val vertx: Vertx
    ) : AbstractExecutorService(), ScheduledExecutorService {
    override fun execute(command: Runnable) {
        workerExecutor.executeBlocking(Callable {
            command.run()
        }, false).onComplete {
            if (it.failed()) {
                throw WorkerExecutorServiceError("Should not happen")
            }
        }
    }

    override fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> =
        WorkerExecutorServiceScheduledFuture(vertx, this, command, delay, unit).apply { schedule() }

    override fun shutdown() {
        throw UnsupportedOperationException("should not be called")
    }

    override fun shutdownNow(): MutableList<Runnable> {
        throw UnsupportedOperationException("should not be called")
    }

    override fun isShutdown(): Boolean {
        throw UnsupportedOperationException("should not be called")
    }

    override fun isTerminated(): Boolean {
        throw UnsupportedOperationException("should not be called")
    }

    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
        throw UnsupportedOperationException("should not be called")
    }

    override fun <V : Any?> schedule(callable: Callable<V>, delay: Long, unit: TimeUnit): ScheduledFuture<V> {
        throw UnsupportedOperationException("should not be called")
    }

    override fun scheduleAtFixedRate(
        command: Runnable,
        initialDelay: Long,
        period: Long,
        unit: TimeUnit
    ): ScheduledFuture<*> {
        throw UnsupportedOperationException("should not be called")
    }

    override fun scheduleWithFixedDelay(
        command: Runnable,
        initialDelay: Long,
        delay: Long,
        unit: TimeUnit
    ): ScheduledFuture<*> {
        throw UnsupportedOperationException("should not be called")
    }

}