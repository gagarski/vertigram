package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Use this method to change the access settings of a managed bot. Returns `true` on success.
 *
 * See Telegram's
 * [setManagedBotAccessSettings](https://core.telegram.org/bots/api#setmanagedbotaccesssettings) documentation.
 */
@TelegramCodegen.Method
data class SetManagedBotAccessSettings internal constructor(
    /** User identifier of the managed bot whose access settings will be changed. */
    val userId: Long,
    /** Pass `true` if only selected users can access the bot. */
    @get:JvmName("getIsAccessRestricted")
    val isAccessRestricted: Boolean,
    /** Up to 10 identifiers of users who can access the bot in addition to its owner. */
    val addedUserIds: List<Long>? = null
) : JsonTelegramCallable<Boolean>()
