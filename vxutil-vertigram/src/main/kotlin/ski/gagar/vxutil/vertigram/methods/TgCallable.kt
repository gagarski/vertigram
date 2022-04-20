package ski.gagar.vxutil.vertigram.methods

/**
 * This class and subclasses are intentionally a class.
 *
 * If you want to change it (which seems natural) be sure to support in in [ski.gagar.vxutil.vertigram.util.TypeHints]
 *
 * [ReturnType] is used in type hints to store return types of methods and also to allow type inference to infer
 * the return type of [ski.gagar.vxutil.vertigram.client.Telegram.call].
 */
sealed class TgCallable<ReturnType>

abstract class JsonTgCallable<ReturnType> : TgCallable<ReturnType>()

abstract class MultipartTgCallable<ReturnType> : TgCallable<ReturnType>()
