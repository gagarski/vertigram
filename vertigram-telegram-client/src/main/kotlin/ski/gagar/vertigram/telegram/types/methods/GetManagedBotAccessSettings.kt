package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotAccessSettings

/**
 * Returns the access settings of a managed bot.
 *
 * See Telegram's
 * [getManagedBotAccessSettings](https://core.telegram.org/bots/api#getmanagedbotaccesssettings) documentation.
 */
@TelegramCodegen.Method
data class GetManagedBotAccessSettings internal constructor(
    /** Unique identifier of the target user whose bot access settings will be returned. */
    val userId: Long
) : JsonTelegramCallable<BotAccessSettings>()
