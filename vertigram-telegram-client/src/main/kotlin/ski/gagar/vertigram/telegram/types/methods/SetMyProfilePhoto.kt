package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Telegram [setMyProfilePhoto](https://core.telegram.org/bots/api#setmyprofilephoto) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetMyProfilePhoto internal constructor(
    val photo: InputMedia.ProfilePhoto
) : MultipartTelegramCallable<Boolean>()
