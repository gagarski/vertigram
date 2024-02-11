package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.types.UserProfilePhotos
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getUserProfilePhotos](https://core.telegram.org/bots/api#getuserprofilephotos) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class GetUserProfilePhotos(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val userId: Long,
    val offset: Long = Defaults.offset,
    val limit: Long = Defaults.limit
) : JsonTelegramCallable<List<UserProfilePhotos>>() {
    object Defaults {
        const val offset = 0L
        const val limit = 100L
    }
}
