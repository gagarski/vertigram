package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.ParseMode
import ski.gagar.vertigram.types.PollType
import ski.gagar.vertigram.types.ReplyMarkup
import ski.gagar.vertigram.types.ReplyParameters
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Telegram [sendPoll](https://core.telegram.org/bots/api#sendpoll) method.
 *
 * Telegram method is divided into two virtual methods: sendPoll and sendQuiz,
 * each of them has a separate cases regarding close date options.
 * The method name is changed for quiz polls because the parameter names do
 * not represent the quiz nature of the poll naturally.
 *
 * Each case has multiple mutually-exclusive cases for different auto-closure settings.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
sealed class SendPoll : JsonTelegramCallable<Message>(), HasChatId {
    /**
     * Cases for regular poll
     */
    @TelegramMethod(
        methodName = "sendPoll"
    )
    @TelegramCodegen(
        methodName = "sendPoll",
        generatePseudoConstructor = true,
        pseudoConstructorName = "SendPoll"
    )
    @Throttled
    sealed class Regular : SendPoll() {
        /**
         * Case with [openPeriod] field
         */
        data class OpenPeriod internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val allowsMultipleAnswers: Boolean = false,
            val openPeriod: Duration,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            val type = PollType.REGULAR
            @get:JvmName("getIsClosed")
            val isClosed = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case with [closeDate] field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendPoll",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendPoll"
        )
        @Throttled
        data class CloseDate internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val allowsMultipleAnswers: Boolean = false,
            val closeDate: Instant,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            val type = PollType.REGULAR
            @get:JvmName("getIsClosed")
            val isClosed = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case with no openPeriod and closeDate field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendPoll",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendPoll"
        )
        @Throttled
        data class Indefinite internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val allowsMultipleAnswers: Boolean = false,
            @get:JvmName("getIsClosed")
            val isClosed: Boolean? = null,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            val type = PollType.REGULAR

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }
    }

    /**
     * Cases for quiz
     */
    sealed class Quiz : SendPoll() {
        /**
         * Case with [openPeriod] field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendQuiz",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class OpenPeriod internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            @PublishedApi internal val explanation: String? = null,
            @PublishedApi internal val explanationParseMode: ParseMode? = null,
            @PublishedApi internal val explanationEntities: List<MessageEntity>? = null,
            val openPeriod: Duration,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            val type = PollType.QUIZ
            @get:JvmName("getIsClosed")
            val isClosed = false
            val allowsMultipleAnswers: Boolean = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case with [closeDate] field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendQuiz",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class CloseDate internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            @PublishedApi internal val explanation: String? = null,
            @PublishedApi internal val explanationParseMode: ParseMode? = null,
            @PublishedApi internal val explanationEntities: List<MessageEntity>? = null,
            val closeDate: Instant,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            val type = PollType.QUIZ
            @get:JvmName("getIsClosed")
            val isClosed = false
            val allowsMultipleAnswers: Boolean = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case with no openPeriod and closeDate field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendQuiz",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class Indefinite internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val chatId: ChatId,
            val messageThreadId: Long? = null,
            val question: String,
            val options: List<String>,
            @get:JvmName("getIsAnonymous")
            val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            @PublishedApi internal val explanation: String? = null,
            @PublishedApi internal val explanationParseMode: ParseMode? = null,
            @PublishedApi internal val explanationEntities: List<MessageEntity>? = null,
            @get:JvmName("getIsClosed")
            val isClosed: Boolean? = null,
            val disableNotification: Boolean = false,
            val protectContent: Boolean = false,
            val replyParameters: ReplyParameters? = null,
            val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            val type = PollType.QUIZ
            val allowsMultipleAnswers: Boolean = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }
    }
}