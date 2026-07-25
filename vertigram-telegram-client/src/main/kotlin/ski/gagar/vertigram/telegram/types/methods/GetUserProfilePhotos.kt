package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.UserProfilePhotos
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method to get a list of profile pictures for a user.
 *
 * See Telegram's [getUserProfilePhotos](https://core.telegram.org/bots/api#getuserprofilephotos) documentation.
 */
@TelegramCodegen.Method
data class GetUserProfilePhotos internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Sequential number of the first photo to be returned. */
    val offset: Long? = null,
    /** Maximum number of photos to retrieve; 1-100. */
    val limit: Long? = null
) : JsonTelegramCallable<List<UserProfilePhotos>>()
