package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [Location](https://core.telegram.org/bots/api#location) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Location(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Duration? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
)
