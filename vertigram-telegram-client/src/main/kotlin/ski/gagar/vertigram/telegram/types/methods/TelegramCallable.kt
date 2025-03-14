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
@TelegramCodegen
@TelegramMethod
sealed class TelegramCallable<ReturnType> {
    suspend fun call(telegram: Telegram) = telegram.call(this)
}

@TelegramCodegen
abstract class JsonTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()

@TelegramCodegen
abstract class MultipartTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()
