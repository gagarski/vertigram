package ski.gagar.vertigram.telegram.client

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vertigram.telegram.types.methods.TelegramCallable
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER

/**
 * A skeleton for [Telegram] implementations.
 *
 * It uses [VertigramTypeHints] to implement [call]
 */
abstract class AbstractTelegram : Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

    /**
     * Abstract method for calling [callable] with known [resultType]
     */
    @Deprecated("Call Telegram.methodName() instead")
    abstract suspend fun <T> call(resultType: JavaType, callable: TelegramCallable<T>): T

    @Deprecated("Call Telegram.methodName() instead")
    override suspend fun <T> call(callable: TelegramCallable<T>): T {
        @Suppress("DEPRECATION")
        return call(VertigramTypeHints.responseTypeByClass.getOrAssert(callable.javaClass), callable)
    }
}
