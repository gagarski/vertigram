package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod

/**
 * Base class for every Telegram method.
 *
 * [ReturnType] is used in type hints to store return types of methods and also to allow type inference to infer
 * the return type of [ski.gagar.vertigram.client.Telegram.call].
 */
@TelegramCodegen
@TelegramMethod
sealed class TelegramCallable<ReturnType>

abstract class JsonTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()

abstract class MultipartTelegramCallable<ReturnType> : TelegramCallable<ReturnType>()
