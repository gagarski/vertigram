package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.UserProfileAudios

/**
 * Use this method to get a list of profile audios for a user.
 *
 * See Telegram's [getUserProfileAudios](https://core.telegram.org/bots/api#getuserprofileaudios) documentation.
 */
@TelegramCodegen.Method
data class GetUserProfileAudios internal constructor(
    /** Unique identifier of the target user. */
    val userId: Long,
    /** Sequential number of the first audio to be returned. */
    val offset: Long? = null,
    /** Maximum number of audios to retrieve; 1-100. */
    val limit: Long? = null
) : JsonTelegramCallable<UserProfileAudios>()
