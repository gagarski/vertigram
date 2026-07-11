package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.OwnedGifts
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [getChatGifts](https://core.telegram.org/bots/api#getchatgifts) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class GetChatGifts internal constructor(
    override val chatId: ChatId,
    val excludeUnsaved: Boolean = false,
    val excludeSaved: Boolean = false,
    val excludeUnlimited: Boolean = false,
    val excludeLimitedUpgradable: Boolean = false,
    val excludeLimitedNonUpgradable: Boolean = false,
    val excludeFromBlockchain: Boolean = false,
    val excludeUnique: Boolean = false,
    val sortByPrice: Boolean = false,
    val offset: String? = null,
    val limit: Int? = null,
) : JsonTelegramCallable<OwnedGifts>(), HasChatId
