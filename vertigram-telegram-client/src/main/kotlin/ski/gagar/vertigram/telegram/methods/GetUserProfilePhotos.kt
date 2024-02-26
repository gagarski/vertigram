package ski.gagar.vertigram.telegram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.telegram.types.UserProfilePhotos
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
    val offset: Long? = null,
    val limit: Long? = null
) : JsonTelegramCallable<List<UserProfilePhotos>>()