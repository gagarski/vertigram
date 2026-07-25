package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * This object represents a point on the map.
 *
 * See Telegram's [Location](https://core.telegram.org/bots/api#location) documentation.
 */
@TelegramCodegen.Type
data class Location internal constructor(
    /** Latitude as defined by the sender. */
    val latitude: Double,
    /** Longitude as defined by the sender. */
    val longitude: Double,
    /** The radius of uncertainty for the location, measured in meters; 0-1500. */
    val horizontalAccuracy: Double? = null,
    /** Time relative to the message sending date during which the location can be updated; for active live locations. */
    val livePeriod: Duration? = null,
    /** The direction in which the user is moving, in degrees; 1-360. For active live locations only. */
    val heading: Int? = null,
    /**
     * The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live
     * locations only.
     */
    val proximityAlertRadius: Int? = null,
) {
    companion object
}
