package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [convertGiftToStars](https://core.telegram.org/bots/api#convertgifttostars) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class ConvertGiftToStars internal constructor(
    val businessConnectionId: String,
    val ownedGiftId: String
) : JsonTelegramCallable<Boolean>()
