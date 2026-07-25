package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents one row of the high scores table for a game.
 *
 * See Telegram's [GameHighScore](https://core.telegram.org/bots/api#gamehighscore) documentation.
 */
@TelegramCodegen.Type
data class GameHighScore internal constructor(
    /** Position in the high score table for the game. */
    val position: Int,
    /** User. */
    val user: User,
    /** Score. */
    val score: Int
) {
    companion object
}
