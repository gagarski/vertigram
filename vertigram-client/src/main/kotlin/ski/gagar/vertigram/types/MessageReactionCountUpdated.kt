package ski.gagar.vertigram.types

import java.time.Instant

data class MessageReactionCountUpdated(
    val chat: Chat,
    val messageId: Long,
    val date: Instant,
    val reactions: List<ReactionType>
)
