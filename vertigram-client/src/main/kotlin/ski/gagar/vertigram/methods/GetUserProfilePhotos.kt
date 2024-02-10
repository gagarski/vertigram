package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.UserProfilePhotos

data class GetUserProfilePhotos(
    val userId: Long,
    val offset: Long = Defaults.offset,
    val limit: Long = Defaults.limit
) : JsonTelegramCallable<List<UserProfilePhotos>>() {
    object Defaults {
        const val offset = 0L
        const val limit = 100L
    }
}
