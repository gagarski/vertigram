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
abstract class AbstractTelegram : ski.gagar.vertigram.telegram.client.Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

    /**
     * Abstract method for calling [callable] with known [resultType]
     */
    abstract suspend fun <T> call(resultType: JavaType, callable: TelegramCallable<T>): T

    override suspend fun <T> call(callable: TelegramCallable<T>): T =
        call(VertigramTypeHints.responseTypeByClass.getOrAssert(callable.javaClass), callable)
}
