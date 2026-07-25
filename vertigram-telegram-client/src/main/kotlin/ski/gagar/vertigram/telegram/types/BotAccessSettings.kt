package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * This object describes the access settings of a bot.
 *
 * See Telegram's [BotAccessSettings](https://core.telegram.org/bots/api#botaccesssettings) documentation.
 */
@TelegramCodegen.Type
data class BotAccessSettings internal constructor(
    /** `true` if only selected users can access the bot. The bot's owner can always access it. */
    @get:JvmName("getIsAccessRestricted")
    val isAccessRestricted: Boolean,
    /** The list of other users who have access to the bot if the access is restricted. */
    val addedUsers: List<User>? = null
) {
    companion object
}
