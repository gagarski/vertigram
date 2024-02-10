package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This class and subclasses are intentionally a class.
 *
 * If you want to change it (which seems natural) be sure to support in in [ski.gagar.vertigram.util.VertigramTypeHints]
 *
 * [ReturnType] is used in type hints to store return types of methods and also to allow type inference to infer
 * the return type of [ski.gagar.vertigram.client.Telegram.call].
 */

sealed class TgCallable<ReturnType>

@TelegramCodegen
abstract class JsonTgCallable<ReturnType> : TgCallable<ReturnType>()

@TelegramCodegen
abstract class MultipartTgCallable<ReturnType> : TgCallable<ReturnType>()
