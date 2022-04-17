package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.PollType
import ski.gagar.vxutil.vertigram.types.ReplyMarkup
import java.time.Duration
import java.time.Instant

@TgMethod
data class SendPoll(
    val chatId: ChatId,
    val question: String,
    val options: List<String>,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = Defaults.isAnonymous,
    val type: PollType,
    val allowsMultipleAnswers: Boolean = false,
    val correctOptionId: Int? = null,
    val explanation: String? = null,
    val explanationParseMode: ParseMode? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Duration? = null,
    val closeDate: Instant? = null,
    @get:JvmName("getIsClosed")
    val isClosed: Boolean? = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message> {

    object Defaults {
        const val isAnonymous = true
    }
}
