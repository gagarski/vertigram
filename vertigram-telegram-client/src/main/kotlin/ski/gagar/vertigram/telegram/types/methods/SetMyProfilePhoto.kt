package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Changes the profile photo of the bot. Returns `true` on success.
 *
 * See Telegram's [setMyProfilePhoto](https://core.telegram.org/bots/api#setmyprofilephoto) documentation.
 */
@TelegramCodegen.Method
data class SetMyProfilePhoto internal constructor(
    /** New profile photo for the bot. */
    val photo: InputMedia.ProfilePhoto
) : MultipartTelegramCallable<Boolean>()
