package ski.gagar.vertigram.types

import java.time.Instant

data class ChatBoostRemoved(
    val chat: Chat,
    val boostId: String,
    val removeDate: Instant,
    val source: ChatBoostSource
)