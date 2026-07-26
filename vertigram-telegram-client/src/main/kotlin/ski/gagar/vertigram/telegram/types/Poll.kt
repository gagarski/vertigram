package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalExplanationWithEntities
import ski.gagar.vertigram.telegram.types.formattedtext.HasQuestionWithEntities
import ski.gagar.vertigram.telegram.types.formattedtext.HasTextWithEntities
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Contains information about a poll.
 *
 * [Regular] and [Quiz] represent the two poll types and their different field sets.
 *
 * See Telegram's [Poll](https://core.telegram.org/bots/api#poll) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Poll.Regular::class, name = Poll.Type.REGULAR_STR),
    JsonSubTypes.Type(value = Poll.Quiz::class, name = Poll.Type.QUIZ_STR)
)
interface Poll : HasQuestionWithEntities {
    val id: String
    override val question: String
    override val questionEntities: List<MessageEntity>?
    val options: List<Option>
    val totalVoterCount: Int
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getIsClosed")
    val isClosed: Boolean
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean
    val type: Type
    val openPeriod: Duration?
    val closeDate: Instant?
    val allowsRevoting: Boolean
    val description: String?
    val descriptionEntities: List<MessageEntity>?
    val media: Media?
    val membersOnly: Boolean
    val countryCodes: List<String>?

    /** Case when the poll is a regular poll. */
    @TelegramCodegen.Type
    data class Regular internal constructor(
        /** Unique poll identifier. */
        override val id: String,
        /** Poll question, 1-300 characters. */
        override val question: String,
        /** Special entities that appear in [question]. */
        override val questionEntities: List<MessageEntity>? = null,
        /** Poll options. */
        override val options: List<Option>,
        /** Total number of users that voted in the poll. */
        override val totalVoterCount: Int,
        /** Whether the poll is closed. */
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        /** Whether the poll is anonymous. */
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        /** Whether the poll allows multiple answers. */
        val allowsMultipleAnswers: Boolean = false,
        /** Time the poll will be active after creation. */
        override val openPeriod: Duration? = null,
        /** Point in time when the poll will be automatically closed. */
        override val closeDate: Instant? = null,
        /** Whether voters can change their vote. */
        override val allowsRevoting: Boolean = false,
        /** Poll description. */
        override val description: String? = null,
        /** Special entities that appear in [description]. */
        override val descriptionEntities: List<MessageEntity>? = null,
        /** Media added to the poll description. */
        override val media: Media? = null,
        /** Whether only chat members can vote in the poll. */
        override val membersOnly: Boolean = false,
        /** Country codes of users allowed to vote in the poll. */
        override val countryCodes: List<String>? = null
    ) : Poll {
        override val type: Type = Type.REGULAR

        companion object
    }

    /** Case when the poll is a quiz. */
    @TelegramCodegen.Type
    data class Quiz internal constructor(
        /** Unique poll identifier. */
        override val id: String,
        /** Poll question, 1-300 characters. */
        override val question: String,
        /** Special entities that appear in [question]. */
        override val questionEntities: List<MessageEntity>? = null,
        /** Poll options. */
        override val options: List<Option>,
        /** Total number of users that voted in the poll. */
        override val totalVoterCount: Int,
        /** Whether the poll is closed. */
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        /** Whether the poll is anonymous. */
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        /** Identifiers of the correct answer options, starting from 0. */
        val correctOptionIds: List<Int>? = null,
        /** Text shown when a user chooses an incorrect answer or taps the lamp icon. */
        override val explanation: String? = null,
        /** Special entities that appear in [explanation]. */
        override val explanationEntities: List<MessageEntity>? = null,
        /** Time the poll will be active after creation. */
        override val openPeriod: Duration? = null,
        /** Point in time when the poll will be automatically closed. */
        override val closeDate: Instant? = null,
        /** Whether voters can change their vote. */
        override val allowsRevoting: Boolean = false,
        /** Poll description. */
        override val description: String? = null,
        /** Special entities that appear in [description]. */
        override val descriptionEntities: List<MessageEntity>? = null,
        /** Media added to the poll description. */
        override val media: Media? = null,
        /** Media added to the quiz explanation. */
        val explanationMedia: Media? = null,
        /** Whether only chat members can vote in the poll. */
        override val membersOnly: Boolean = false,
        /** Country codes of users allowed to vote in the poll. */
        override val countryCodes: List<String>? = null
    ) : Poll, HasOptionalExplanationWithEntities {
        override val type: Type = Type.QUIZ

        companion object
    }

    /**
     * Contains information about one answer option in a poll.
     *
     * See Telegram's [PollOption](https://core.telegram.org/bots/api#polloption) documentation.
     */
    @TelegramCodegen.Type
    data class Option internal constructor(
        /** Unique identifier of the option, persistent across option addition and deletion. */
        val persistentId: String,
        /** Option text, 1-100 characters. */
        override val text: String,
        /** Special entities that appear in [text]. */
        override val entities: List<MessageEntity>? = null,
        /** Media added to the poll option. */
        val media: Media? = null,
        /** Number of users that voted for this option; may be 0 if unknown. */
        val voterCount: Int,
        /** User that added the option. */
        val addedByUser: User? = null,
        /** Chat that added the option. */
        val addedByChat: Chat? = null,
        /** Point in time when the option was added. */
        val additionDate: Instant? = null
    ) : HasTextWithEntities {
        companion object
    }

    /**
     * Contains information about media in a poll.
     *
     * See Telegram's [PollMedia](https://core.telegram.org/bots/api#pollmedia) documentation.
     */
    @TelegramCodegen.Type
    data class Media internal constructor(
        /** Media is an animation. */
        val animation: Animation? = null,
        /** Media is an audio file. */
        val audio: Audio? = null,
        /** Media is a document. */
        val document: Document? = null,
        /** Media is an HTTP link. */
        val link: Link? = null,
        /** Media is a live photo. */
        val livePhoto: LivePhoto? = null,
        /** Media is a shared location. */
        val location: Location? = null,
        /** Media is a photo. */
        val photo: List<PhotoSize>? = null,
        /** Media is a sticker. */
        val sticker: Sticker? = null,
        /** Media is a venue. */
        val venue: Venue? = null,
        /** Media is a video. */
        val video: Video? = null
    ) {
        /**
         * Represents an HTTP link in poll media.
         *
         * See Telegram's [Link](https://core.telegram.org/bots/api#link) documentation.
         */
        @TelegramCodegen.Type
        data class Link internal constructor(
            /** URL of the link. */
            val url: String
        ) {
            companion object
        }

        companion object
    }



    /**
     * Represents an answer of a user or an anonymous voter in a non-anonymous poll.
     *
     * See Telegram's [PollAnswer](https://core.telegram.org/bots/api#pollanswer) documentation.
     */
    @TelegramCodegen.Type
    data class Answer internal constructor(
        /** Unique poll identifier. */
        val pollId: String,
        /** Chat that changed the answer to the poll, for anonymous voters. */
        val voterChat: Chat? = null,
        /** Identifiers of chosen answer options. */
        val optionIds: List<Int>,
        /** Persistent identifiers of chosen answer options. */
        val optionPersistentIds: List<String>? = null,
        /** User that changed the answer to the poll. */
        val user: User? = null
    ) {
        companion object
    }


    /**
     * A value for [type].
     */
    enum class Type {
        @JsonProperty(REGULAR_STR)
        REGULAR,
        @JsonProperty(QUIZ_STR)
        QUIZ;

        companion object {
            const val REGULAR_STR = "regular"
            const val QUIZ_STR = "quiz"
        }
    }
}
