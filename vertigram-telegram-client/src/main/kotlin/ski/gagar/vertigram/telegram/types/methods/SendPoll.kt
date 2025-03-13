package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.MessageEntity
import ski.gagar.vertigram.telegram.types.Poll
import ski.gagar.vertigram.telegram.types.ReplyMarkup
import ski.gagar.vertigram.telegram.types.ReplyParameters
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichExplanation
import ski.gagar.vertigram.telegram.types.richtext.HasRichQuestion
import ski.gagar.vertigram.telegram.types.richtext.HasRichText
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs
import ski.gagar.vertigram.util.jackson.typing.TypeResolverWithDeductionBuilder
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = SendPoll.Regular.OpenPeriod::class, name = Poll.Type.REGULAR_STR),
    JsonSubTypes.Type(value = SendPoll.Regular.CloseDate::class, name = Poll.Type.REGULAR_STR),
    JsonSubTypes.Type(value = SendPoll.Regular.Indefinite::class, name = Poll.Type.REGULAR_STR),
    JsonSubTypes.Type(value = SendPoll.Quiz.OpenPeriod::class, name = Poll.Type.QUIZ_STR),
    JsonSubTypes.Type(value = SendPoll.Quiz.CloseDate::class, name = Poll.Type.QUIZ_STR),
    JsonSubTypes.Type(value = SendPoll.Quiz.Indefinite::class, name = Poll.Type.QUIZ_STR),
)
@JsonTypeResolver(TypeResolverWithDeductionBuilder::class)
@TelegramCodegen
sealed class SendPoll : JsonTelegramCallable<Message>(), HasChatId, HasRichQuestion {
    abstract val businessConnectionId: String?
    abstract val messageThreadId: Long?
    abstract override val question: String
    abstract override val questionParseMode: RichText.ParseMode?
    abstract override val questionEntities: List<MessageEntity>?
    abstract val options: List<InputOption>
    abstract val isAnonymous: Boolean
    abstract val allowsMultipleAnswers: Boolean
    abstract val disableNotification: Boolean
    abstract val protectContent: Boolean
    abstract val messageEffectId: String?
    abstract val replyParameters: ReplyParameters?
    abstract val replyMarkup: ReplyMarkup?
    abstract val type: Poll.Type

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
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            override val allowsMultipleAnswers: Boolean = false,
            val openPeriod: Duration,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null,

            ) : Regular() {
            override val type = Poll.Type.REGULAR

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
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            override val allowsMultipleAnswers: Boolean = false,
            val closeDate: Instant,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            override val type = Poll.Type.REGULAR

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
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            override val allowsMultipleAnswers: Boolean = false,
            val isClosed: Boolean = false,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            override val type = Poll.Type.REGULAR

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }
    }

    /**
     * Cases for quiz
     */
    @TelegramCodegen
    sealed class Quiz : SendPoll(), HasOptionalRichExplanation {
        /**
         * Case with [openPeriod] field
         */
        @TelegramMethod(
            methodName = "sendPoll"
        )
        @TelegramCodegen(
            methodName = "sendQuiz",
            docMethodName = "sendPoll",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class OpenPeriod internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            override val explanation: String? = null,
            override val explanationParseMode: RichText.ParseMode? = null,
            override val explanationEntities: List<MessageEntity>? = null,
            val openPeriod: Duration,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ
            override val allowsMultipleAnswers: Boolean = false

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
            docMethodName = "sendPoll",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class CloseDate internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            override val explanation: String? = null,
            override val explanationParseMode: RichText.ParseMode? = null,
            override val explanationEntities: List<MessageEntity>? = null,
            val closeDate: Instant,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ
            override val allowsMultipleAnswers: Boolean = false

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
            docMethodName = "sendPoll",
            generatePseudoConstructor = true,
            pseudoConstructorName = "SendQuiz"
        )
        @Throttled
        data class Indefinite internal constructor(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            override val businessConnectionId: String? = null,
            override val chatId: ChatId,
            override val messageThreadId: Long? = null,
            override val question: String,
            override val questionParseMode: RichText.ParseMode? = null,
            override val questionEntities: List<MessageEntity>? = null,
            override val options: List<InputOption>,
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            val correctOptionId: Int,
            override val explanation: String? = null,
            override val explanationParseMode: RichText.ParseMode? = null,
            override val explanationEntities: List<MessageEntity>? = null,
            @get:JvmName("getIsClosed")
            val isClosed: Boolean = false,
            override val disableNotification: Boolean = false,
            override val protectContent: Boolean = false,
            override val messageEffectId: String? = null,
            override val replyParameters: ReplyParameters? = null,
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ
            override val allowsMultipleAnswers: Boolean = false

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }
    }

    /**
     * Telegram [InputPollOption](https://core.telegram.org/bots/api#inputpolloption) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class InputOption internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val text: String,
        override val parseMode: RichText.ParseMode? = null,
        override val entities: List<MessageEntity>? = null,
    ) : HasRichText {
        companion object
    }

}
