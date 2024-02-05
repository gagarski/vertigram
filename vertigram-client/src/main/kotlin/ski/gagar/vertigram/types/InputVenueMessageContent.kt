package ski.gagar.vertigram.types

data class InputVenueMessageContent(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null
) : InputMessageContent
