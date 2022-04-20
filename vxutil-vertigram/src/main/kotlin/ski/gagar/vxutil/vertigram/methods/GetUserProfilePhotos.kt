package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.UserProfilePhotos

@TgMethod
data class GetUserProfilePhotos(
    val userId: Long,
    val offset: Long = Defaults.offset,
    val limit: Long = Defaults.limit
) : JsonTgCallable<List<UserProfilePhotos>>() {
    object Defaults {
        const val offset = 0L
        const val limit = 100L
    }
}
