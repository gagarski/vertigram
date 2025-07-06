package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMedia
import ski.gagar.vertigram.telegram.types.InputMedia

/**
 * Telegram [setBusinessAccountProfilePhoto](https://core.telegram.org/bots/api#setbusinessaccountprofilephoto) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class SetBusinessAccountProfilePhoto internal constructor(
    val businessConnectionId: String,
    @TelegramMedia
    val photo: InputMedia.Photo,
    @get:JvmName("getIsPublic")
    val isPublic: Boolean = false,
) : MultipartTelegramCallable<Boolean>()
