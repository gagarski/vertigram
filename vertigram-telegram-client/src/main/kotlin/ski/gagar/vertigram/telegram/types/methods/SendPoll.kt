package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.*
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedExplanation
import ski.gagar.vertigram.telegram.types.formattedtext.HasFormattedQuestion
import ski.gagar.vertigram.telegram.types.formattedtext.HasFormattedText
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.jackson.typing.TypeResolverWithDeductionBuilder
import java.time.Duration
import java.time.Instant

/**
 * Use this method to send a native poll. On success, the sent [Message] is returned.
 *
 * Vertigram represents regular polls with [ski.gagar.vertigram.telegram.methods.sendPoll] and quiz-style polls with
 * [ski.gagar.vertigram.telegram.methods.sendQuiz]. Each method has separate cases for polls with an open period, a
 * close date, or neither.
 *
 * See Telegram's [sendPoll](https://core.telegram.org/bots/api#sendpoll) documentation.
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
@TelegramCodegen.Method
sealed class SendPoll : JsonTelegramCallable<Message>(), HasChatId, HasFormattedQuestion {
    abstract val businessConnectionId: String?
    abstract val messageThreadId: Long?
    abstract override val question: String
    abstract override val questionParseMode: FormattedText.ParseMode?
    abstract override val questionEntities: List<MessageEntity>?
    abstract val options: List<InputOption>
    abstract val isAnonymous: Boolean
    abstract val allowsMultipleAnswers: Boolean
    abstract val allowsRevoting: Boolean
    abstract val shuffleOptions: Boolean
    abstract val hideResultsUntilCloses: Boolean
    abstract val membersOnly: Boolean
    abstract val countryCodes: List<String>?
    abstract val description: String?
    abstract val descriptionParseMode: FormattedText.ParseMode?
    abstract val descriptionEntities: List<MessageEntity>?
    abstract val media: InputMedia.Poll?
    abstract val disableNotification: Boolean
    abstract val protectContent: Boolean
    abstract val allowPaidBroadcast: Boolean
    abstract val messageEffectId: String?
    abstract val replyParameters: ReplyParameters?
    abstract val replyMarkup: ReplyMarkup?
    abstract val type: Poll.Type

    /**
     * Cases for regular poll
     */
    @TelegramCodegen.Method
    @Throttled
    sealed class Regular : SendPoll() {
        abstract val allowAddingOptions: Boolean

        /** Case when [openPeriod] is specified. */
        @TelegramCodegen.Method(
            name = "sendPoll"
        )
        data class OpenPeriod internal constructor(
                /** Unique identifier of the business connection on behalf of which the message will be sent. */
                override val businessConnectionId: String? = null,
                /**
                 * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls
                 * can't be sent to channel direct messages chats.
                 */
                override val chatId: ChatId,
                /**
                 * Unique identifier for the target message thread (topic) of a forum; for forum supergroups and private
                 * chats of bots with forum topic mode enabled only.
                 */
                override val messageThreadId: Long? = null,
                /** Poll question, 1-300 characters. */
                override val question: String,
                /**
                 * Mode for parsing entities in the question. See Telegram's
                 * [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
                 * Currently, only custom emoji entities are allowed.
                 */
                override val questionParseMode: FormattedText.ParseMode? = null,
                /**
                 * List of special entities that appear in the poll question. It can be specified instead of
                 * [questionParseMode].
                 */
                override val questionEntities: List<MessageEntity>? = null,
                /** List of 1-12 answer options. */
                override val options: List<InputOption>,
                /** `true` if the poll needs to be anonymous. */
                @get:JvmName("getIsAnonymous")
                override val isAnonymous: Boolean = Defaults.isAnonymous,
                /** Pass `true` if the poll allows multiple answers. */
                override val allowsMultipleAnswers: Boolean = false,
                /**
                 * Pass `true` if the poll allows to change chosen answer options.
                 */
                override val allowsRevoting: Boolean = Defaults.allowsRevoting,
                /** Pass `true` if the poll options must be shown in random order. */
                override val shuffleOptions: Boolean = false,
                /**
                 * Pass `true` if answer options can be added to the poll after creation; not supported for anonymous
                 * polls and quizzes.
                 */
                override val allowAddingOptions: Boolean = false,
                /** Pass `true` if poll results must be shown only after the poll closes. */
                override val hideResultsUntilCloses: Boolean = false,
                /**
                 * Pass `true` if voting is limited to users who have been members of the chat where the poll is being
                 * sent for more than 24 hours; for channel chats only.
                 */
                override val membersOnly: Boolean = false,
                /**
                 * List of 0-12 two-letter ISO 3166-1 alpha-2 country codes indicating the countries from which users
                 * can vote in the poll; for channel chats only. Use `FT` as a country code to allow users with anonymous
                 * numbers to vote. If omitted or empty, then users from any country can participate in the poll.
                 */
                override val countryCodes: List<String>? = null,
                /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
                override val description: String? = null,
                /**
                 * Mode for parsing entities in the poll description. See Telegram's
                 * [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details.
                 */
                override val descriptionParseMode: FormattedText.ParseMode? = null,
                /**
                 * List of special entities that appear in the poll description, which can be specified instead of
                 * [descriptionParseMode].
                 */
                override val descriptionEntities: List<MessageEntity>? = null,
                /** Media added to the poll description. */
                override val media: InputMedia.Poll? = null,
                /**
                 * Amount of time the poll will be active after creation, from 5 seconds to 30 days. Can't be used
                 * together with a close date.
                 */
                val openPeriod: Duration,
                /** Sends the message silently. Users will receive a notification with no sound. */
                override val disableNotification: Boolean = false,
                /** Protects the contents of the sent message from forwarding and saving. */
                override val protectContent: Boolean = false,
                /**
                 * Pass `true` to allow up to 1000 messages per second, ignoring broadcasting limits for a fee of 0.1
                 * Telegram Stars per message. The relevant Stars will be withdrawn from the bot's balance.
                 */
                override val allowPaidBroadcast: Boolean = false,
                /** Unique identifier of the message effect to be added to the message; for private chats only. */
                override val messageEffectId: String? = null,
                /** Description of the message to reply to. */
                override val replyParameters: ReplyParameters? = null,
                /**
                 * Additional interface options for an inline keyboard, custom reply keyboard, instructions to remove a
                 * reply keyboard or to force a reply from the user.
                 */
                override val replyMarkup: ReplyMarkup? = null,

        ) : Regular() {
            override val type = Poll.Type.REGULAR

            object Defaults {
                const val isAnonymous: Boolean = true
                const val allowsRevoting: Boolean = true
            }
        }

        /** Case when [closeDate] is specified. */
        @TelegramCodegen.Method(
            name = "sendPoll"
        )
        @Throttled
        data class CloseDate internal constructor(
                /** Unique identifier of the business connection on behalf of which the message will be sent. */
                override val businessConnectionId: String? = null,
                /**
                 * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls
                 * can't be sent to channel direct messages chats.
                 */
                override val chatId: ChatId,
                /** Unique identifier for the target message thread (topic) of a forum. */
                override val messageThreadId: Long? = null,
                /** Poll question, 1-300 characters. */
                override val question: String,
                /** Mode for parsing entities in the question. Currently, only custom emoji entities are allowed. */
                override val questionParseMode: FormattedText.ParseMode? = null,
                /** List of special entities that appear in the poll question. */
                override val questionEntities: List<MessageEntity>? = null,
                /** List of 1-12 answer options. */
                override val options: List<InputOption>,
                /** `true` if the poll needs to be anonymous. */
                @get:JvmName("getIsAnonymous")
                override val isAnonymous: Boolean = Defaults.isAnonymous,
                /** Pass `true` if the poll allows multiple answers. */
                override val allowsMultipleAnswers: Boolean = false,
                /** Pass `true` if the poll allows to change chosen answer options. */
                override val allowsRevoting: Boolean = Defaults.allowsRevoting,
                /** Pass `true` if the poll options must be shown in random order. */
                override val shuffleOptions: Boolean = false,
                /** Pass `true` if answer options can be added to the poll after creation. */
                override val allowAddingOptions: Boolean = false,
                /** Pass `true` if poll results must be shown only after the poll closes. */
                override val hideResultsUntilCloses: Boolean = false,
                /** Pass `true` if voting is limited to users who have been chat members for more than 24 hours. */
                override val membersOnly: Boolean = false,
                /** List of country codes indicating the countries from which users can vote in the poll. */
                override val countryCodes: List<String>? = null,
                /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
                override val description: String? = null,
                /** Mode for parsing entities in the poll description. */
                override val descriptionParseMode: FormattedText.ParseMode? = null,
                /** List of special entities that appear in the poll description. */
                override val descriptionEntities: List<MessageEntity>? = null,
                /** Media added to the poll description. */
                override val media: InputMedia.Poll? = null,
                /** Point in time when the poll will be automatically closed. */
                val closeDate: Instant,
                /** Sends the message silently. Users will receive a notification with no sound. */
                override val disableNotification: Boolean = false,
                /** Protects the contents of the sent message from forwarding and saving. */
                override val protectContent: Boolean = false,
                /** Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message. */
                override val allowPaidBroadcast: Boolean = false,
                /** Unique identifier of the message effect to be added to the message; for private chats only. */
                override val messageEffectId: String? = null,
                /** Description of the message to reply to. */
                override val replyParameters: ReplyParameters? = null,
                /** Additional interface options. */
                override val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            override val type = Poll.Type.REGULAR

            object Defaults {
                const val isAnonymous: Boolean = true
                const val allowsRevoting: Boolean = true
            }
        }

        /** Case when neither `openPeriod` nor `closeDate` is specified. */
        @TelegramCodegen.Method(
            name = "sendPoll"
        )
        @Throttled
        data class Indefinite internal constructor(
                /** Unique identifier of the business connection on behalf of which the message will be sent. */
                override val businessConnectionId: String? = null,
                /**
                 * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls
                 * can't be sent to channel direct messages chats.
                 */
                override val chatId: ChatId,
                /** Unique identifier for the target message thread (topic) of a forum. */
                override val messageThreadId: Long? = null,
                /** Poll question, 1-300 characters. */
                override val question: String,
                /** Mode for parsing entities in the question. Currently, only custom emoji entities are allowed. */
                override val questionParseMode: FormattedText.ParseMode? = null,
                /** List of special entities that appear in the poll question. */
                override val questionEntities: List<MessageEntity>? = null,
                /** List of 1-12 answer options. */
                override val options: List<InputOption>,
                /** `true` if the poll needs to be anonymous. */
                @get:JvmName("getIsAnonymous")
                override val isAnonymous: Boolean = Defaults.isAnonymous,
                /** Pass `true` if the poll allows multiple answers. */
                override val allowsMultipleAnswers: Boolean = false,
                /** Pass `true` if the poll allows to change chosen answer options. */
                override val allowsRevoting: Boolean = Defaults.allowsRevoting,
                /** Pass `true` if the poll options must be shown in random order. */
                override val shuffleOptions: Boolean = false,
                /** Pass `true` if answer options can be added to the poll after creation. */
                override val allowAddingOptions: Boolean = false,
                /** Pass `true` if poll results must be shown only after the poll closes. */
                override val hideResultsUntilCloses: Boolean = false,
                /** Pass `true` if voting is limited to users who have been chat members for more than 24 hours. */
                override val membersOnly: Boolean = false,
                /** List of country codes indicating the countries from which users can vote in the poll. */
                override val countryCodes: List<String>? = null,
                /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
                override val description: String? = null,
                /** Mode for parsing entities in the poll description. */
                override val descriptionParseMode: FormattedText.ParseMode? = null,
                /** List of special entities that appear in the poll description. */
                override val descriptionEntities: List<MessageEntity>? = null,
                /** Media added to the poll description. */
                override val media: InputMedia.Poll? = null,
                /** Pass `true` if the poll needs to be immediately closed. This can be useful for poll preview. */
                @get:JvmName("getIsClosed")
                val isClosed: Boolean = false,
                /** Sends the message silently. Users will receive a notification with no sound. */
                override val disableNotification: Boolean = false,
                /** Protects the contents of the sent message from forwarding and saving. */
                override val protectContent: Boolean = false,
                /** Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message. */
                override val allowPaidBroadcast: Boolean = false,
                /** Unique identifier of the message effect to be added to the message; for private chats only. */
                override val messageEffectId: String? = null,
                /** Description of the message to reply to. */
                override val replyParameters: ReplyParameters? = null,
                /** Additional interface options. */
                override val replyMarkup: ReplyMarkup? = null
        ) : Regular() {
            override val type = Poll.Type.REGULAR

            object Defaults {
                const val isAnonymous: Boolean = true
                const val allowsRevoting: Boolean = true
            }
        }
    }

    /**
     * Cases for quiz
     */
    @TelegramCodegen.Method
    sealed class Quiz : SendPoll(), HasOptionalFormattedExplanation {
        /**
         * Case when [openPeriod] is specified.
         */
        @TelegramCodegen.Method(
            name = "sendQuiz",
            telegramName = "sendPoll"
        )
        @Throttled
        data class OpenPeriod internal constructor(
            /** Unique identifier of the business connection on behalf of which the message will be sent. */
            override val businessConnectionId: String? = null,
            /**
             * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls can't
             * be sent to channel direct messages chats.
             */
            override val chatId: ChatId,
            /** Unique identifier for the target message thread (topic) of a forum. */
            override val messageThreadId: Long? = null,
            /** Poll question, 1-300 characters. */
            override val question: String,
            /** Mode for parsing entities in the question. Currently, only custom emoji entities are allowed. */
            override val questionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll question. */
            override val questionEntities: List<MessageEntity>? = null,
            /** List of 1-12 answer options. */
            override val options: List<InputOption>,
            /** `true` if the poll needs to be anonymous. */
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            /** List of monotonically increasing 0-based identifiers of the correct answer options. */
            val correctOptionIds: List<Int>,
            /** Pass `true` if the poll allows multiple answers. */
            override val allowsMultipleAnswers: Boolean = false,
            /** Pass `true` if the poll allows to change chosen answer options. */
            override val allowsRevoting: Boolean = false,
            /** Pass `true` if the poll options must be shown in random order. */
            override val shuffleOptions: Boolean = false,
            /** Pass `true` if poll results must be shown only after the poll closes. */
            override val hideResultsUntilCloses: Boolean = false,
            /** Pass `true` if voting is limited to users who have been chat members for more than 24 hours. */
            override val membersOnly: Boolean = false,
            /** List of country codes indicating the countries from which users can vote in the poll. */
            override val countryCodes: List<String>? = null,
            /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
            override val description: String? = null,
            /** Mode for parsing entities in the poll description. */
            override val descriptionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll description. */
            override val descriptionEntities: List<MessageEntity>? = null,
            /** Media added to the poll description. */
            override val media: InputMedia.Poll? = null,
            /**
             * Text that is shown when a user chooses an incorrect answer or taps on the lamp icon in a quiz-style poll,
             * 0-200 characters with at most 2 line feeds after entities parsing.
             */
            override val explanation: String? = null,
            /** Mode for parsing entities in the explanation. */
            override val explanationParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll explanation. */
            override val explanationEntities: List<MessageEntity>? = null,
            /** Media added to the quiz explanation. */
            val explanationMedia: InputMedia.Poll? = null,
            /** Amount of time the poll will be active after creation, from 5 seconds to 30 days. */
            val openPeriod: Duration,
            /** Sends the message silently. Users will receive a notification with no sound. */
            override val disableNotification: Boolean = false,
            /** Protects the contents of the sent message from forwarding and saving. */
            override val protectContent: Boolean = false,
            /** Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message. */
            override val allowPaidBroadcast: Boolean = false,
            /** Unique identifier of the message effect to be added to the message; for private chats only. */
            override val messageEffectId: String? = null,
            /** Description of the message to reply to. */
            override val replyParameters: ReplyParameters? = null,
            /** Additional interface options. */
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case when [closeDate] is specified.
         */
        @TelegramCodegen.Method(
            name = "sendQuiz",
            telegramName = "sendPoll"
        )
        @Throttled
        data class CloseDate internal constructor(
            /** Unique identifier of the business connection on behalf of which the message will be sent. */
            override val businessConnectionId: String? = null,
            /**
             * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls can't
             * be sent to channel direct messages chats.
             */
            override val chatId: ChatId,
            /** Unique identifier for the target message thread (topic) of a forum. */
            override val messageThreadId: Long? = null,
            /** Poll question, 1-300 characters. */
            override val question: String,
            /** Mode for parsing entities in the question. Currently, only custom emoji entities are allowed. */
            override val questionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll question. */
            override val questionEntities: List<MessageEntity>? = null,
            /** List of 1-12 answer options. */
            override val options: List<InputOption>,
            /** `true` if the poll needs to be anonymous. */
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            /** List of monotonically increasing 0-based identifiers of the correct answer options. */
            val correctOptionIds: List<Int>,
            /** Pass `true` if the poll allows multiple answers. */
            override val allowsMultipleAnswers: Boolean = false,
            /** Pass `true` if the poll allows to change chosen answer options. */
            override val allowsRevoting: Boolean = false,
            /** Pass `true` if the poll options must be shown in random order. */
            override val shuffleOptions: Boolean = false,
            /** Pass `true` if poll results must be shown only after the poll closes. */
            override val hideResultsUntilCloses: Boolean = false,
            /** Pass `true` if voting is limited to users who have been chat members for more than 24 hours. */
            override val membersOnly: Boolean = false,
            /** List of country codes indicating the countries from which users can vote in the poll. */
            override val countryCodes: List<String>? = null,
            /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
            override val description: String? = null,
            /** Mode for parsing entities in the poll description. */
            override val descriptionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll description. */
            override val descriptionEntities: List<MessageEntity>? = null,
            /** Media added to the poll description. */
            override val media: InputMedia.Poll? = null,
            /** Text that is shown when a user chooses an incorrect answer or taps on the lamp icon. */
            override val explanation: String? = null,
            /** Mode for parsing entities in the explanation. */
            override val explanationParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll explanation. */
            override val explanationEntities: List<MessageEntity>? = null,
            /** Media added to the quiz explanation. */
            val explanationMedia: InputMedia.Poll? = null,
            /** Point in time when the poll will be automatically closed. */
            val closeDate: Instant,
            /** Sends the message silently. Users will receive a notification with no sound. */
            override val disableNotification: Boolean = false,
            /** Protects the contents of the sent message from forwarding and saving. */
            override val protectContent: Boolean = false,
            /** Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message. */
            override val allowPaidBroadcast: Boolean = false,
            /** Unique identifier of the message effect to be added to the message; for private chats only. */
            override val messageEffectId: String? = null,
            /** Description of the message to reply to. */
            override val replyParameters: ReplyParameters? = null,
            /** Additional interface options. */
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }

        /**
         * Case when neither `openPeriod` nor `closeDate` is specified.
         */
        @TelegramCodegen.Method(
            name = "sendQuiz",
            telegramName = "sendPoll"
        )
        @Throttled
        data class Indefinite internal constructor(
            /** Unique identifier of the business connection on behalf of which the message will be sent. */
            override val businessConnectionId: String? = null,
            /**
             * Unique identifier for the target chat or username of the target bot, supergroup, or channel. Polls can't
             * be sent to channel direct messages chats.
             */
            override val chatId: ChatId,
            /** Unique identifier for the target message thread (topic) of a forum. */
            override val messageThreadId: Long? = null,
            /** Poll question, 1-300 characters. */
            override val question: String,
            /** Mode for parsing entities in the question. Currently, only custom emoji entities are allowed. */
            override val questionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll question. */
            override val questionEntities: List<MessageEntity>? = null,
            /** List of 1-12 answer options. */
            override val options: List<InputOption>,
            /** `true` if the poll needs to be anonymous. */
            @get:JvmName("getIsAnonymous")
            override val isAnonymous: Boolean = Defaults.isAnonymous,
            /** List of monotonically increasing 0-based identifiers of the correct answer options. */
            val correctOptionIds: List<Int>,
            /** Pass `true` if the poll allows multiple answers. */
            override val allowsMultipleAnswers: Boolean = false,
            /** Pass `true` if the poll allows to change chosen answer options. */
            override val allowsRevoting: Boolean = false,
            /** Pass `true` if the poll options must be shown in random order. */
            override val shuffleOptions: Boolean = false,
            /** Pass `true` if poll results must be shown only after the poll closes. */
            override val hideResultsUntilCloses: Boolean = false,
            /** Pass `true` if voting is limited to users who have been chat members for more than 24 hours. */
            override val membersOnly: Boolean = false,
            /** List of country codes indicating the countries from which users can vote in the poll. */
            override val countryCodes: List<String>? = null,
            /** Description of the poll to be sent, 0-1024 characters after entities parsing. */
            override val description: String? = null,
            /** Mode for parsing entities in the poll description. */
            override val descriptionParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll description. */
            override val descriptionEntities: List<MessageEntity>? = null,
            /** Media added to the poll description. */
            override val media: InputMedia.Poll? = null,
            /** Text that is shown when a user chooses an incorrect answer or taps on the lamp icon. */
            override val explanation: String? = null,
            /** Mode for parsing entities in the explanation. */
            override val explanationParseMode: FormattedText.ParseMode? = null,
            /** List of special entities that appear in the poll explanation. */
            override val explanationEntities: List<MessageEntity>? = null,
            /** Media added to the quiz explanation. */
            val explanationMedia: InputMedia.Poll? = null,
            /** Pass `true` if the poll needs to be immediately closed. This can be useful for poll preview. */
            @get:JvmName("getIsClosed")
            val isClosed: Boolean = false,
            /** Sends the message silently. Users will receive a notification with no sound. */
            override val disableNotification: Boolean = false,
            /** Protects the contents of the sent message from forwarding and saving. */
            override val protectContent: Boolean = false,
            /** Pass `true` to allow up to 1000 messages per second for a fee of 0.1 Telegram Stars per message. */
            override val allowPaidBroadcast: Boolean = false,
            /** Unique identifier of the message effect to be added to the message; for private chats only. */
            override val messageEffectId: String? = null,
            /** Description of the message to reply to. */
            override val replyParameters: ReplyParameters? = null,
            /** Additional interface options. */
            override val replyMarkup: ReplyMarkup? = null
        ) : Quiz() {
            override val type = Poll.Type.QUIZ

            object Defaults {
                const val isAnonymous: Boolean = true
            }
        }
    }

    /**
     * This object contains information about one answer option in a poll to be sent.
     *
     * See Telegram's [InputPollOption](https://core.telegram.org/bots/api#inputpolloption) documentation.
     */
    @TelegramCodegen.Type
    data class InputOption internal constructor(
        /** Option text, 1-100 characters. */
        override val text: String,
        /**
         * Mode for parsing entities in the text. See Telegram's
         * [formatting options](https://core.telegram.org/bots/api#formatting-options) for more details. Currently, only
         * custom emoji entities are allowed.
         */
        val textParseMode: FormattedText.ParseMode? = null,
        /**
         * List of special entities that appear in the poll option text. It can be specified instead of
         * [textParseMode].
         */
        val textEntities: List<MessageEntity>? = null,
        /** Media added to the poll option. */
        val media: InputMedia.PollOption? = null,
    ) : HasFormattedText {
        @get:JsonIgnore
        override val parseMode: FormattedText.ParseMode?
            get() = textParseMode

        @get:JsonIgnore
        override val entities: List<MessageEntity>?
            get() = textEntities

        companion object
    }

}
