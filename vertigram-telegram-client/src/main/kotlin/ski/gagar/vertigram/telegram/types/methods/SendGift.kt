package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * Telegram [sendGift](https://core.telegram.org/bots/api#sendgift) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = SendGift.User::class),
    JsonSubTypes.Type(value = SendGift.Chat::class),
)
sealed interface SendGift : HasOptionalRichText {
    val giftId: String
    val payForUpgrade: Boolean

    /**
     * Case when [userId] is set
     */
    @TelegramCodegen.Method(name = "sendGift")
    data class User internal constructor(
        val userId: Long,
        override val giftId: String,
        override val payForUpgrade: Boolean = false,
        override val text: String? = null,
        override val textParseMode: RichText.ParseMode? = null,
        override val textEntities: List<MessageEntity>? = null
    ) : SendGift, JsonTelegramCallable<Boolean>()


    /**
     * Case when [chatId] is set
     */
    @TelegramCodegen.Method(name = "sendGift")
    data class Chat internal constructor(
        override val chatId: ChatId,
        override val giftId: String,
        override val payForUpgrade: Boolean = false,
        override val text: String? = null,
        override val textParseMode: RichText.ParseMode? = null,
        override val textEntities: List<MessageEntity>? = null
    ) : SendGift, JsonTelegramCallable<Boolean>(), HasChatId
}
