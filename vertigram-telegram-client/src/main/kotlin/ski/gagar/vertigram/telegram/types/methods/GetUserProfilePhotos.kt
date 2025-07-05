package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.UserProfilePhotos
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [getUserProfilePhotos](https://core.telegram.org/bots/api#getuserprofilephotos) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetUserProfilePhotos internal constructor(
    val userId: Long,
    val offset: Long? = null,
    val limit: Long? = null
) : JsonTelegramCallable<List<UserProfilePhotos>>()