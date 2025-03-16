package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Duration

/**
 * Telegram [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Method(wrapRichText = false)
data class AnswerCallbackQuery internal constructor(
    val callbackQueryId: String,
    val text: String? = null,
    val showAlert: Boolean? = null,
    val url: String? = null,
    val cacheTime: Duration? = null
) : JsonTelegramCallable<Boolean>()
