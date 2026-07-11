package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalExplanationWithEntities
import ski.gagar.vertigram.telegram.types.richtext.HasQuestionWithEntities
import ski.gagar.vertigram.telegram.types.richtext.HasTextWithEntities
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Telegram [Poll](https://core.telegram.org/bots/api#poll) type.
 *
 * Synthetic subtypes are intrudoced for [Poll.Regular] and [Poll.Quiz] poll, since they can have a different set of fields.
 *
 * Also classes directly related to polls are nested
 *
 * For up-to-date documentation, please consult the official Telegram docs.
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

    /**
     * Regular case
     */

    @TelegramCodegen.Type
    data class Regular internal constructor(
        override val id: String,
        override val question: String,
        override val questionEntities: List<MessageEntity>? = null,
        override val options: List<Option>,
        override val totalVoterCount: Int,
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        val allowsMultipleAnswers: Boolean = false,
        override val openPeriod: Duration? = null,
        override val closeDate: Instant? = null,
        override val allowsRevoting: Boolean = false,
        override val description: String? = null,
        override val descriptionEntities: List<MessageEntity>? = null,
        override val media: Media? = null,
        override val membersOnly: Boolean = false,
        override val countryCodes: List<String>? = null
    ) : Poll {
        override val type: Type = Type.REGULAR

        companion object
    }

    /**
     * Quiz case
     */
    @TelegramCodegen.Type
    data class Quiz internal constructor(
        override val id: String,
        override val question: String,
        override val questionEntities: List<MessageEntity>? = null,
        override val options: List<Option>,
        override val totalVoterCount: Int,
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        val correctOptionIds: List<Int>? = null,
        override val explanation: String? = null,
        override val explanationEntities: List<MessageEntity>? = null,
        override val openPeriod: Duration? = null,
        override val closeDate: Instant? = null,
        override val allowsRevoting: Boolean = false,
        override val description: String? = null,
        override val descriptionEntities: List<MessageEntity>? = null,
        override val media: Media? = null,
        val explanationMedia: Media? = null,
        override val membersOnly: Boolean = false,
        override val countryCodes: List<String>? = null
    ) : Poll, HasOptionalExplanationWithEntities {
        override val type: Type = Type.QUIZ

        companion object
    }

    /**
     * Telegram [PollOption](https://core.telegram.org/bots/api#polloption) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Option internal constructor(
        val persistentId: String,
        override val text: String,
        override val entities: List<MessageEntity>? = null,
        val media: Media? = null,
        val voterCount: Int,
        val addedByUser: User? = null,
        val addedByChat: Chat? = null,
        val additionDate: Instant? = null
    ) : HasTextWithEntities {
        companion object
    }

    /**
     * Telegram [PollMedia](https://core.telegram.org/bots/api#pollmedia) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Media internal constructor(
        val animation: Animation? = null,
        val audio: Audio? = null,
        val document: Document? = null,
        val link: Link? = null,
        val livePhoto: LivePhoto? = null,
        val location: Location? = null,
        val photo: List<PhotoSize>? = null,
        val sticker: Sticker? = null,
        val venue: Venue? = null,
        val video: Video? = null
    ) {
        companion object
    }

    /**
     * Telegram [PollAnswer](https://core.telegram.org/bots/api#pollanswer) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Answer internal constructor(
        val pollId: String,
        val voterChat: Chat? = null,
        val optionIds: List<Int>,
        val optionPersistentIds: List<String>? = null,
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
