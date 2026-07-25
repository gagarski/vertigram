package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.BusinessConnection

/**
 * Converts a given regular gift to Telegram Stars. Requires the
 * [BusinessConnection.BotRights.canConvertGiftsToStars] business bot right. Returns `true` on success.
 *
 * See Telegram's [convertGiftToStars](https://core.telegram.org/bots/api#convertgifttostars) documentation.
 */
@TelegramCodegen.Method()
data class ConvertGiftToStars internal constructor(
    /** Unique identifier of the business connection. */
    val businessConnectionId: String,
    /** Unique identifier of the regular gift that should be converted to Telegram Stars. */
    val ownedGiftId: String
) : JsonTelegramCallable<Boolean>()
