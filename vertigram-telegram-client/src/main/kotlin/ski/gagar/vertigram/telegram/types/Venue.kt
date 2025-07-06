package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [Venue](https://core.telegram.org/bots/api#venue) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Venue internal constructor(
    val location: Location,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null
) {
    companion object {}
}
