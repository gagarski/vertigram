package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.client.Telegram

/**
 * Base class for every Telegram method.
 *
 * [ReturnType] is used in type hints to store return types of methods and also to allow type inference to infer
 * the return type of [ski.gagar.vertigram.telegram.client.Telegram.call].
 */
@TelegramMethod
@TelegramCodegen.Method
sealed class TelegramCallable<ReturnType> {
    @Suppress("DEPRECATION")
    suspend fun call(telegram: Telegram) = telegram.call(this)
}

@TelegramCodegen.Method
abstract class JsonTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()

@TelegramCodegen.Method
abstract class MultipartTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()
