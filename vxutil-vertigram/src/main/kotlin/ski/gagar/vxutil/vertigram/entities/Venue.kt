package ski.gagar.vxutil.vertigram.entities

data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    val foursquareId: String?,
    val foursquareType: String?
)
