package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class MessageReactionUpdated(
    val chat: Chat,
    val messageId: Long,
    val user: User,
    val actorChat: Chat,
    val date: Instant,
    val oldReaction: List<ReactionType>,
    val newReaction: List<ReactionType>
)
