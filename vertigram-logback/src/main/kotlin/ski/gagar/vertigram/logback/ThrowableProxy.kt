package ski.gagar.vertigram.logback

import ch.qos.logback.classic.spi.IThrowableProxy
import ch.qos.logback.classic.spi.StackTraceElementProxy
import ch.qos.logback.classic.spi.ThrowableProxyUtil

data class ThrowableProxy(
    val message: String,
    val className: String,
    val stackTrace: List<StackTraceElement>,
    val cause: ThrowableProxy? = null,
    val commonFrames: Int = 0,
    val suppressed: List<ThrowableProxy>? = null
)

fun ThrowableProxy(orig: IThrowableProxy?): ThrowableProxy? =
    orig?.let {
        ThrowableProxy(
            message = orig.message,
            className = orig.className,
            stackTrace = orig.stackTraceElementProxyArray.asSequence().map { it.stackTraceElement }.toList(),
            cause = ThrowableProxy(orig.cause),
            commonFrames = orig.commonFrames,
            suppressed = orig.suppressed?.mapNotNull { ThrowableProxy(it) }?.toList()
        )
    }

internal class IThrowableProxyImpl(private val p: ThrowableProxy) : IThrowableProxy {
    override fun getMessage(): String = p.message

    override fun getClassName(): String = p.className

    override fun getStackTraceElementProxyArray(): Array<StackTraceElementProxy> =
        p.stackTrace.asSequence().map { StackTraceElementProxy(it) }.toList().toTypedArray()

    override fun getCommonFrames(): Int = p.commonFrames

    override fun getCause(): IThrowableProxy? = p.cause?.let { IThrowableProxyImpl(it) }

    override fun getSuppressed(): Array<IThrowableProxy>? =
        p.suppressed?.asSequence()?.map { IThrowableProxyImpl(it) }?.toList()?.toTypedArray()

    override fun isCyclic(): Boolean = false
}

fun ThrowableProxy.asString(): String = ThrowableProxyUtil.asString(IThrowableProxyImpl(this))
