package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Represents a venue.
 *
 * See Telegram's [Venue](https://core.telegram.org/bots/api#venue) documentation.
 */
@TelegramCodegen.Type
data class Venue internal constructor(
    /** Venue location. */
    val location: Location,
    /** Name of the venue. */
    val title: String,
    /** Address of the venue. */
    val address: String,
    /** Foursquare identifier of the venue. */
    val foursquareId: String? = null,
    /** Foursquare type of the venue. */
    val foursquareType: String? = null,
    /** Google Places identifier of the venue. */
    val googlePlaceId: String? = null,
    /** Google Places type of the venue. */
    val googlePlaceType: String? = null
) {
    companion object {}
}
