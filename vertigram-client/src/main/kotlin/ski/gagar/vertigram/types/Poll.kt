package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.richtext.HasOptionalExplanationWithEntities
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
    override val explanation: String? = null,
    override val explanationEntities: List<MessageEntity>? = null,
    val openPeriod: Duration? = null,
    val closeDate: Instant? = null
) : HasOptionalExplanationWithEntities
