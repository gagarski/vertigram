package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BotAccessSettings

/**
 * Telegram [getManagedBotAccessSettings](https://core.telegram.org/bots/api#getmanagedbotaccesssettings) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetManagedBotAccessSettings internal constructor(
    val userId: Long
) : JsonTelegramCallable<BotAccessSettings>()
