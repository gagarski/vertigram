package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

/**
 * Telegram type PollOption.
 */
data class PollOption(
    val text: String,
    val voterCount: Long
)

/**
 * Telegram type PollAnswer.
 */
data class PollAnswer(
    val pollId: String,
    val user: User,
    val optionIds: List<Long>
)

/**
 * Available values for [Poll.type]
 */
enum class PollType{
    @JsonProperty("regular")
    REGULAR,
    @JsonProperty("quiz")
    QUIZ
}

/**
 * Telegram type Poll.
 */
data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val totalVoterCount: Long,
    @get:JvmName("getIsClosed")
    val isClosed: Boolean = false,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val type: PollType,
    val allowsMultipleAnswers: Boolean = false,
    val correctOptionId: Long? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Long? = null,
    val closeDate: Instant? = null
)

val Poll.explanationEntitiesInstantiated: List<InstantiatedEntity>
    get() = explanationEntities?.map { InstantiatedEntity(it, this.explanation) } ?: listOf()
