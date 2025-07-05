package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [GameHighScore](https://core.telegram.org/bots/api#gamehighscore) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class GameHighScore internal constructor(
    val position: Int,
    val user: User,
    val score: Int
) {
    companion object
}
