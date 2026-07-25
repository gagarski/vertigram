package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Game
import java.time.Duration

/**
 * Use this method to send answers to callback queries sent from inline keyboards. The answer will be displayed to the
 * user as a notification at the top of the chat screen or as an alert. On success, `true` is returned.
 *
 * Alternatively, the user can be redirected to the specified Game URL. For this option to work, you must first create
 * a game for your bot via [@BotFather](https://t.me/botfather) and accept the terms. Otherwise, you may use links like
 * `t.me/your_bot?start=XXXX` that open your bot with a parameter.
 *
 * See Telegram's [answerCallbackQuery](https://core.telegram.org/bots/api#answercallbackquery) documentation.
 */
@TelegramCodegen.Method
data class AnswerCallbackQuery internal constructor(
    /** Unique identifier for the query to be answered. */
    val callbackQueryId: String,
    /** Text of the notification. If not specified, nothing will be shown to the user, 0-200 characters. */
    val text: String? = null,
    /** If `true`, an alert will be shown by the client instead of a notification at the top of the chat screen. */
    val showAlert: Boolean? = null,
    /**
     * URL that will be opened by the user's client. If you have created a [Game] and accepted the conditions via
     * [@BotFather](https://t.me/botfather), specify the URL that opens your game; this will only work if the query comes
     * from a `callback_game` button. Otherwise, you may use links like `t.me/your_bot?start=XXXX` that open your bot with
     * a parameter.
     */
    val url: String? = null,
    /**
     * The maximum amount of time that the result of the callback query may be cached client-side. Telegram apps support
     * caching starting in version 3.14.
     */
    val cacheTime: Duration? = null
) : JsonTelegramCallable<Boolean>()
