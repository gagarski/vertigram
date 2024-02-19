package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ProximityAlertTriggered](https://core.telegram.org/bots/api#proximityalerttriggered) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ProximityAlertTriggered(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val traveler: User,
    val watcher: User,
    val distance: Int
)
