package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [setManagedBotAccessSettings](https://core.telegram.org/bots/api#setmanagedbotaccesssettings) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetManagedBotAccessSettings internal constructor(
    val userId: Long,
    @get:JvmName("getIsAccessRestricted")
    val isAccessRestricted: Boolean,
    val addedUserIds: List<Long>? = null
) : JsonTelegramCallable<Boolean>()
