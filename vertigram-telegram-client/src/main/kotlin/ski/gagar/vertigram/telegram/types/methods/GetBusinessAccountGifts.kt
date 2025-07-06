package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.OwnedGifts

/**
 * Telegram [getBusinessAccountGifts](https://core.telegram.org/bots/api#getbusinessaccountgifts) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method()
data class GetBusinessAccountGifts internal constructor(
    val businessConnectionId: String,
    val excludeUnsaved: Boolean = false,
    val excludeSaved: Boolean = false,
    val excludeUnlimited: Boolean = false,
    val excludeLimited: Boolean = false,
    val excludeUnique: Boolean = false,
    val sortByPrice: Boolean = false,
    val offset: String? = null,
    val limit: Int? = null,
) : JsonTelegramCallable<OwnedGifts>()
