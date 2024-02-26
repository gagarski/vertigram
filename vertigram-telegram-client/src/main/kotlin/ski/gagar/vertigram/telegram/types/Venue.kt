package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Venue](https://core.telegram.org/bots/api#venue) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Venue(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val location: Location,
    val title: String,
    val address: String,
    val foursquareId: String? = null,
    val foursquareType: String? = null,
    val googlePlaceId: String? = null,
    val googlePlaceType: String? = null
)
