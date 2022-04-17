package ski.gagar.vxutil.vertigram.types

import java.time.Duration
import java.time.Instant

data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val totalVoterCount: Int,
    @get:JvmName("getIsClosed")
    val isClosed: Boolean = false,
    @get:JvmName("getIsAnonymous")
    val isAnonymous: Boolean = false,
    val type: PollType,
    val allowsMultipleAnswers: Boolean = false,
    val correctOptionId: Int? = null,
    val explanation: String? = null,
    val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Duration? = null,
    val closeDate: Instant? = null
)

val Poll.explanationEntitiesInstantiated: List<InstantiatedEntity>
    get() = explanationEntities?.map { InstantiatedEntity(it, this.explanation) } ?: listOf()
