package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.MessageEntity
import ski.gagar.vxutil.vertigram.types.ParseMode
import ski.gagar.vxutil.vertigram.types.PollType
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class SendPoll(
    val chatId: ChatId,
    val question: String,
    val options: List<String>,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = true,
    val type: PollType,
    val allowsMultipleAnswers: Boolean = false,
    val correctOptionId: Long? = null,
    val explanation: String? = null,
    val explanationParseMode: ParseMode? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Long? = null,
    val closeDate: Long? = null,
    @get:JvmName("getIsClosed")
    val isClosed: Boolean? = false,
    val disableNotification: Boolean = false,
    val protectContent: Boolean = false,
    val replyToMessageId: Long? = null,
    val allowSendingWithoutReply: Boolean = false,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>()
