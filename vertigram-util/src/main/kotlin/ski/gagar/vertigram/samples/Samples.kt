package ski.gagar.vertigram.samples
import ski.gagar.vertigram.util.lazy
import ski.gagar.vertigram.util.logger

@Suppress("UNUSED_PARAMETER")
private fun doOperation(obj: Any) {}
/**
 * An example for [lazy]
 */
private fun lazyLog(obj: Any) {
    try {
        logger.lazy.info { "Performing an operation with $obj" }
        doOperation(obj)
    } catch (ex: Exception) {
        logger.lazy.error(ex) { "Something bad happened with $obj" }
    }
}