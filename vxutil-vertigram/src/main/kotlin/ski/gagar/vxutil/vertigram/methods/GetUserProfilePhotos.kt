package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.UserProfilePhotos

data class GetUserProfilePhotos(
    val userId: Long,
    val offset: Long = 0,
    val limit: Long = 100
) : JsonTgCallable<List<UserProfilePhotos>>()
