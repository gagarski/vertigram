package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen(
    wrapRichText = false
)
data class AnswerCallbackQuery(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val callbackQueryId: String,
    val text: String? = null,
    val showAlert: Boolean? = null,
    val url: String? = null,
    val cacheTime: Duration? = null
) : JsonTelegramCallable<Boolean>()
