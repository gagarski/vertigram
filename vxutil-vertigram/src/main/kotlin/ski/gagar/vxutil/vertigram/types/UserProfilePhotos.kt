package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type UserProfilePhotos.
 */
data class UserProfilePhotos(
    val totalCount: Long,
    val photos: List<List<PhotoSize>>
)
