package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedText
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Sends a gift to the given user or channel chat. The gift can't be converted to Telegram Stars by the receiver.
 *
 * Returns `true` on success.
 *
 * See Telegram's [sendGift](https://core.telegram.org/bots/api#sendgift) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = SendGift.User::class),
    JsonSubTypes.Type(value = SendGift.Chat::class),
)
sealed interface SendGift : HasOptionalFormattedText {
    val giftId: String
    val payForUpgrade: Boolean

    /**
     * Case when the gift is sent to a user.
     */
    @TelegramCodegen.Method(name = "sendGift")
    data class User internal constructor(
        /** Unique identifier of the target user who will receive the gift. */
        val userId: Long,
        /** Identifier of the gift. */
        override val giftId: String,
        /** Pass `true` to pay for the gift upgrade from the bot's balance. */
        override val payForUpgrade: Boolean = false,
        /** Text shown with the gift, 0-128 characters. */
        override val text: String? = null,
        /** Mode for parsing entities in [text]. */
        override val textParseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [text]; can be specified instead of [textParseMode]. */
        override val textEntities: List<MessageEntity>? = null
    ) : SendGift, JsonTelegramCallable<Boolean>()


    /**
     * Case when the gift is sent to a channel chat.
     */
    @TelegramCodegen.Method(name = "sendGift")
    data class Chat internal constructor(
        /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
        override val chatId: ChatId,
        /** Identifier of the gift. */
        override val giftId: String,
        /** Pass `true` to pay for the gift upgrade from the bot's balance. */
        override val payForUpgrade: Boolean = false,
        /** Text shown with the gift, 0-128 characters. */
        override val text: String? = null,
        /** Mode for parsing entities in [text]. */
        override val textParseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [text]; can be specified instead of [textParseMode]. */
        override val textEntities: List<MessageEntity>? = null
    ) : SendGift, JsonTelegramCallable<Boolean>(), HasChatId
}
