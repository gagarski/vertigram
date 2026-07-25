package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.OwnedGifts
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Returns the gifts owned by a chat.
 *
 * See Telegram's [getChatGifts](https://core.telegram.org/bots/api#getchatgifts) documentation.
 */
@TelegramCodegen.Method
data class GetChatGifts internal constructor(
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Pass `true` to exclude gifts that aren't saved to the chat's profile page. */
    val excludeUnsaved: Boolean = false,
    /** Pass `true` to exclude gifts that are saved to the chat's profile page. */
    val excludeSaved: Boolean = false,
    /** Pass `true` to exclude gifts that can be purchased an unlimited number of times. */
    val excludeUnlimited: Boolean = false,
    /** Pass `true` to exclude limited gifts that can be upgraded to unique. */
    val excludeLimitedUpgradable: Boolean = false,
    /** Pass `true` to exclude limited gifts that can't be upgraded to unique. */
    val excludeLimitedNonUpgradable: Boolean = false,
    /** Pass `true` to exclude gifts assigned from the TON blockchain. */
    val excludeFromBlockchain: Boolean = false,
    /** Pass `true` to exclude unique gifts. */
    val excludeUnique: Boolean = false,
    /** Pass `true` to sort results by gift price instead of send date. */
    val sortByPrice: Boolean = false,
    /** Offset received from the previous request; use an empty string for the first chunk. */
    val offset: String? = null,
    /** Maximum number of gifts to return; 1-100. */
    val limit: Int? = null,
) : JsonTelegramCallable<OwnedGifts>(), HasChatId
