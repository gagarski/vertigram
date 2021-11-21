package ski.gagar.vxutil

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.invoke.MethodHandles

val globalLogger: Logger
    inline get() = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass())

val Any.logger: Logger
    get() = LoggerFactory.getLogger(javaClass)