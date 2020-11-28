package ski.gagar.vertigram.entities

data class UserProfilePhotos(
    val totalCount: Long,
    val photos: List<List<PhotoSize>>
)