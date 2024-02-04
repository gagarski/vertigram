package ski.gagar.vertigram.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import io.vertx.core.Vertx
import ski.gagar.vertigram.jackson.publishJson

abstract class AbstractEventBusAppender : AppenderBase<ILoggingEvent>() {
    var address: String = "ski.gagar.vertigram.logback"
    abstract val vertx: Vertx?

    override fun append(eventObject: ILoggingEvent) {
        if (null != eventObject.mdcPropertyMap[BYPASS]) {
            return
        }
        vertx?.eventBus()?.publishJson(address, LogEvent(eventObject))
    }

    companion object {
        const val BYPASS = "bypassEventBusAppender"
    }
}
