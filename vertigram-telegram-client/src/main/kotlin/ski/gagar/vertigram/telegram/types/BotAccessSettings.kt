package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [BotAccessSettings](https://core.telegram.org/bots/api#botaccesssettings) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class BotAccessSettings internal constructor(
    @get:JvmName("getIsAccessRestricted")
    val isAccessRestricted: Boolean,
    val addedUsers: List<User>? = null
) {
    companion object
}
