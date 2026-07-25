package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Removes the current profile photo of a managed business account. Requires the
 * [ski.gagar.vertigram.telegram.types.BusinessConnection.BotRights.canEditProfilePhoto] business bot right.
 * Returns `true` on success.
 *
 * See Telegram's
 * [removeBusinessAccountProfilePhoto](https://core.telegram.org/bots/api#removebusinessaccountprofilephoto)
 * documentation.
 */
@TelegramCodegen.Method
data class RemoveBusinessAccountProfilePhoto internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Pass `true` to remove the public photo, which is visible even if the account's main photo is hidden. */
    @get:JvmName("getIsPublic")
    val isPublic: Boolean = false,
) : JsonTelegramCallable<Boolean>()
