package ski.gagar.vertigram.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ski.gagar.vertigram.Vertigram

abstract class AbstractEventBusAppender : AppenderBase<ILoggingEvent>() {
    var address: String = "ski.gagar.vertigram.logback"
    abstract val vertigram: Vertigram?

    override fun append(eventObject: ILoggingEvent) {
        if (null != eventObject.mdcPropertyMap[BYPASS]) {
            return
        }
        vertigram?.eventBus?.publish(address, LogEvent(eventObject))
    }

    companion object {
        const val BYPASS = "bypassEventBusAppender"
    }
}
