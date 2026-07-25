package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Changes the profile photo of a managed business account. Returns `true` on success.
 *
 * See Telegram's
 * [setBusinessAccountProfilePhoto](https://core.telegram.org/bots/api#setbusinessaccountprofilephoto) documentation.
 */
@TelegramCodegen.Method
data class SetBusinessAccountProfilePhoto internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** New profile photo for the business account. */
    val photo: InputMedia.Photo,
    /** Pass `true` to set the public photo visible when the account's main photo is hidden. */
    @get:JvmName("getIsPublic")
    val isPublic: Boolean = false,
) : MultipartTelegramCallable<Boolean>()
