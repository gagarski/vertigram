package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.UserProfileAudios

/**
 * Telegram [getUserProfileAudios](https://core.telegram.org/bots/api#getuserprofileaudios) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetUserProfileAudios internal constructor(
    val userId: Long,
    val offset: Long? = null,
    val limit: Long? = null
) : JsonTelegramCallable<UserProfileAudios>()
