package ski.gagar.vxutil.vertigram.types

data class UserProfilePhotos(
    val totalCount: Int,
    val photos: List<List<PhotoSize>>
)
