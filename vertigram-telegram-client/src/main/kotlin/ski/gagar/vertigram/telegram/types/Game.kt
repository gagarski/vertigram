package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalTextWithEntities

/**
 * This object represents a game. Use BotFather to create and edit games, their short names will act as unique
 * identifiers.
 *
 * See the official Telegram Bot API documentation for [Game](https://core.telegram.org/bots/api#game).
 */
@TelegramCodegen.Type
data class Game internal constructor(
    /** Title of the game. */
    val title: String,
    /** Description of the game. */
    val description: String,
    /** Photo that will be displayed in the game message in chats. */
    val photo: List<PhotoSize>,
    /**
     * Brief description of the game or high scores included in the game message, 0-4096 characters. Can be
     * automatically edited to include current high scores when the bot calls
     * [ski.gagar.vertigram.telegram.methods.setGameScore], or manually edited using
     * [ski.gagar.vertigram.telegram.methods.editMessageText].
     */
    override val text: String? = null,
    /** Special entities that appear in [text], such as usernames, URLs, and bot commands. */
    val textEntities: List<MessageEntity>? = null,
    /** Animation that will be displayed in the game message in chats. */
    val animation: Animation? = null
) : HasOptionalTextWithEntities {
    @JsonIgnore
    override val entities = textEntities

    companion object
}
