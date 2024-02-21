package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.richtext.HasOptionalExplanationWithEntities
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
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Poll.Regular::class, name = Poll.Type.REGULAR_STR),
    JsonSubTypes.Type(value = Poll.Quiz::class, name = Poll.Type.QUIZ_STR)
)
interface Poll {
    val id: String
    val question: String
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

    /**
     * Regular case
     */

    data class Regular(
        override val id: String,
        override val question: String,
        override val options: List<Option>,
        override val totalVoterCount: Int,
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        val allowsMultipleAnswers: Boolean = false,
        override val openPeriod: Duration? = null,
        override val closeDate: Instant? = null
    ) : Poll {
        override val type: Type = Type.REGULAR

        companion object
    }

    /**
     * Quiz case
     */
    data class Quiz(
        override val id: String,
        override val question: String,
        override val options: List<Option>,
        override val totalVoterCount: Int,
        @get:JvmName("getIsClosed")
        override val isClosed: Boolean = false,
        @get:JvmName("getIsAnonymous")
        override val isAnonymous: Boolean = false,
        val correctOptionId: Int? = null,
        override val explanation: String? = null,
        override val explanationEntities: List<MessageEntity>? = null,
        override val openPeriod: Duration? = null,
        override val closeDate: Instant? = null
    ) : Poll, HasOptionalExplanationWithEntities {
        override val type: Type = Type.QUIZ

        companion object
    }

    /**
     * Telegram [PollOption](https://core.telegram.org/bots/api#polloption) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Option(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val text: String,
        val voterCount: Int
    )

    /**
     * Telegram [PollAnswer](https://core.telegram.org/bots/api#pollanswer) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Answer(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val pollId: String,
        val voterChat: Chat? = null,
        val optionIds: List<Int>,
        val user: User? = null
    )


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