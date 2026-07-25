package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Contains a list of user profile photos.
 *
 * See Telegram's [UserProfilePhotos](https://core.telegram.org/bots/api#userprofilephotos) documentation.
 */
@TelegramCodegen.Type
data class UserProfilePhotos internal constructor(
    /** Total number of profile pictures for the target user. */
    val totalCount: Int,
    /** Requested profile pictures, each represented by the available photo sizes. */
    val photos: List<List<PhotoSize>>
) {
    companion object
}
