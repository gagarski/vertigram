package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [UserProfilePhotos](https://core.telegram.org/bots/api#userprofilephotos) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class UserProfilePhotos(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val totalCount: Int,
    val photos: List<List<PhotoSize>>
)
