package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.types.colors.RgbColor
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [ForumTopic](https://core.telegram.org/bots/api#forumtopic) type.
 *
 * This class has all the class related to forum topics nested
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class ForumTopic(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val messageThreadId: Long,
    val name: String,
    val iconColor: RgbColor,
    val iconCustomEmojiId: String? = null
) {
    /**
     * Telegram [ForumTopicClosed](https://core.telegram.org/bots/api#forumtopicclosed) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object Closed

    /**
     * Value for [ski.gagar.vertigram.methods.CreateForumTopic.iconColor],
     * limited according to the Telegram docs.
     */
    enum class Color(val color: RgbColor) {
        CYAN(RgbColor(0x6F.toUByte(), 0xB9.toUByte(), 0xF0.toUByte())),
        YELLOW(RgbColor(0xFF.toUByte(), 0xD6.toUByte(), 0x7E.toUByte())),
        PURPLE(RgbColor(0xC8.toUByte(), 0x86.toUByte(), 0xDB.toUByte())),
        GREEN(RgbColor(0x8E.toUByte(), 0xEE.toUByte(), 0x98.toUByte())),
        PINK(RgbColor(0xFF.toUByte(), 0x93.toUByte(), 0xB2.toUByte())),
        RED(RgbColor(0xFB.toUByte(), 0x6F.toUByte(), 0x5F.toUByte()));

        @JsonValue
        fun toValue() = color.toInt()
    }

    /**
     * Telegram [ForumTopicCreated](https://core.telegram.org/bots/api#forumtopiccreated) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Created(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val name: String,
        val iconColor: RgbColor,
        val iconCustomEmojiId: String? = null
    )

    /**
     * Telegram [ForumTopicEdited](https://core.telegram.org/bots/api#forumtopicedited) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Edited(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val name: String? = null,
        val iconCustomEmojiId: String? = null
    )

    /**
     * Telegram [ForumTopicReopened](https://core.telegram.org/bots/api#forumtopicreopened) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object Reopened

    /**
     * Telegram [GeneralForumTopicHidden](https://core.telegram.org/bots/api#generalforumtopichidden) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object GeneralHidden

    /**
     * Telegram [GeneralForumTopicUnhidden](https://core.telegram.org/bots/api#generalforumtopicunhidden) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data object GeneralUnhidden

}
