package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class ChatBoost(
    val boostId: String,
    val addDate: Instant,
    val expirationDate: Instant,
    val source: ChatBoostSource
)
