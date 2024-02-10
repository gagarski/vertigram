package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vertigram.methods.TelegramCallable
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER

abstract class AbstractTelegram : Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

    abstract suspend fun <T> call(type: JavaType, callable: TelegramCallable<T>): T

    override suspend fun <T> call(callable: TelegramCallable<T>): T =
        call(VertigramTypeHints.responseTypeByClass.getOrAssert(callable.javaClass), callable)
}
