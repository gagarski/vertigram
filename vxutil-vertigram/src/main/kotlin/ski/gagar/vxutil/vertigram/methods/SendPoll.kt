package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.throttling.Throttled
import ski.gagar.vxutil.vertigram.types.*
import java.time.Duration
import java.time.Instant

@TgMethod
@Throttled
data class SendPoll(
    override val chatId: ChatId,
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
    val replyMarkup: ReplyMarkup? = null,
    // Since Telegram Bot Api 6.3
    val messageThreadId: Long? = null,
    // Since Telegram Bot API 7.0
    val replyParameters: ReplyParameters? = null
) : JsonTgCallable<Message>(), HasChatId {

    object Defaults {
        const val isAnonymous = true
    }
}
