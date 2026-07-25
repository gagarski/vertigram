package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object represents a forum topic.
 *
 * See Telegram's [ForumTopic](https://core.telegram.org/bots/api#forumtopic) documentation.
 */
@TelegramCodegen.Type
data class ForumTopic internal constructor(
    /** Unique identifier of the forum topic. */
    val messageThreadId: Long,
    /** Name of the topic. */
    val name: String,
    /** Color of the topic icon in RGB format. */
    val iconColor: RgbColor,
    /** Unique identifier of the custom emoji shown as the topic icon. */
    val iconCustomEmojiId: String? = null,
    /** `true` if the topic name wasn't specified explicitly by its creator and likely needs to be changed by the bot. */
    @get:JvmName("getIsNameImplicit")
    val isNameImplicit: Boolean = false
) {
    /**
     * Value for [ski.gagar.vertigram.telegram.types.methods.CreateForumTopic.iconColor],
     * limited according to the Telegram docs.
     */
    enum class Color(val color: RgbColor) {
        /** Cyan topic icon color. */
        CYAN(RgbColor(0x6F.toUByte(), 0xB9.toUByte(), 0xF0.toUByte())),
        /** Yellow topic icon color. */
        YELLOW(RgbColor(0xFF.toUByte(), 0xD6.toUByte(), 0x7E.toUByte())),
        /** Purple topic icon color. */
        PURPLE(RgbColor(0xC8.toUByte(), 0x86.toUByte(), 0xDB.toUByte())),
        /** Green topic icon color. */
        GREEN(RgbColor(0x8E.toUByte(), 0xEE.toUByte(), 0x98.toUByte())),
        /** Pink topic icon color. */
        PINK(RgbColor(0xFF.toUByte(), 0x93.toUByte(), 0xB2.toUByte())),
        /** Red topic icon color. */
        RED(RgbColor(0xFB.toUByte(), 0x6F.toUByte(), 0x5F.toUByte()));

        @JsonValue
        fun toValue() = color.toInt()
    }

    companion object
}
