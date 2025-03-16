package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [UserProfilePhotos](https://core.telegram.org/bots/api#userprofilephotos) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class UserProfilePhotos internal constructor(
    val totalCount: Int,
    val photos: List<List<PhotoSize>>
) {
    companion object
}
