package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type GameHighScore.
 */
data class GameHighScore(
    val position: Long,
    val user: User,
    val score: Long
)
