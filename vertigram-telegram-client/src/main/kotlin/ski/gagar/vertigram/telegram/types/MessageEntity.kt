package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant

/**
 * Represents one special entity in a text message, such as a hashtag, username, URL, or formatting.
 *
 * See Telegram's [MessageEntity](https://core.telegram.org/bots/api#messageentity) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = MessageEntity.Mention::class, name = MessageEntity.Type.MENTION_STR),
    JsonSubTypes.Type(value = MessageEntity.Hashtag::class, name = MessageEntity.Type.HASHTAG_STR),
    JsonSubTypes.Type(value = MessageEntity.Cashtag::class, name = MessageEntity.Type.CASHTAG_STR),
    JsonSubTypes.Type(value = MessageEntity.BotCommand::class, name = MessageEntity.Type.BOT_COMMAND_STR),
    JsonSubTypes.Type(value = MessageEntity.Url::class, name = MessageEntity.Type.URL_STR),
    JsonSubTypes.Type(value = MessageEntity.Email::class, name = MessageEntity.Type.EMAIL_STR),
    JsonSubTypes.Type(value = MessageEntity.PhoneNumber::class, name = MessageEntity.Type.PHONE_NUMBER_STR),
    JsonSubTypes.Type(value = MessageEntity.Bold::class, name = MessageEntity.Type.BOLD_STR),
    JsonSubTypes.Type(value = MessageEntity.Italic::class, name = MessageEntity.Type.ITALIC_STR),
    JsonSubTypes.Type(value = MessageEntity.Underline::class, name = MessageEntity.Type.UNDERLINE_STR),
    JsonSubTypes.Type(value = MessageEntity.Strikethrough::class, name = MessageEntity.Type.STRIKETHROUGH_STR),
    JsonSubTypes.Type(value = MessageEntity.Spoiler::class, name = MessageEntity.Type.SPOILER_STR),
    JsonSubTypes.Type(value = MessageEntity.Code::class, name = MessageEntity.Type.CODE_STR),
    JsonSubTypes.Type(value = MessageEntity.Pre::class, name = MessageEntity.Type.PRE_STR),
    JsonSubTypes.Type(value = MessageEntity.TextLink::class, name = MessageEntity.Type.TEXT_LINK_STR),
    JsonSubTypes.Type(value = MessageEntity.TextMention::class, name = MessageEntity.Type.TEXT_MENTION_STR),
    JsonSubTypes.Type(value = MessageEntity.CustomEmoji::class, name = MessageEntity.Type.CUSTOM_EMOJI_STR),
    JsonSubTypes.Type(value = MessageEntity.DateTime::class, name = MessageEntity.Type.DATE_TIME_STR),
    JsonSubTypes.Type(value = MessageEntity.BlockQuote::class, name = MessageEntity.Type.BLOCKQUOTE_STR),
    JsonSubTypes.Type(value = MessageEntity.ExpandableBlockQuote::class, name = MessageEntity.Type.EXPANDABLE_BLOCKQUOTE_STR),
)
sealed interface MessageEntity {
    /** Type of the entity. */
    val type: Type
    /** Offset in UTF-16 code units to the start of the entity. */
    val offset: Int
    /** Length of the entity in UTF-16 code units. */
    val length: Int

    fun copyTo(offset: Int = this.offset, length: Int = this.length): MessageEntity

    /** Case when the entity is a mention such as `@username`. */
    data class Mention internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.MENTION
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a hashtag such as `#hashtag`. */
    data class Hashtag internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.HASHTAG
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a cashtag such as `$USD`. */
    data class Cashtag internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.CASHTAG
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a bot command such as `/start@jobs_bot`. */
    data class BotCommand internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BOT_COMMAND
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a URL such as `https://telegram.org`. */
    data class Url internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.URL
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is an email address such as `do-not-reply@telegram.org`. */
    data class Email internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.EMAIL
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a phone number such as `+1-212-555-0123`. */
    data class PhoneNumber internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.PHONE_NUMBER
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is bold text. */
    data class Bold internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BOLD
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is italic text. */
    data class Italic internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.ITALIC
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is underlined text. */
    data class Underline internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.UNDERLINE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is strikethrough text. */
    data class Strikethrough internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.STRIKETHROUGH
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is spoiler text. */
    data class Spoiler internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.SPOILER
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is monowidth text. */
    data class Code internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.CODE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a monowidth block. */
    data class Pre internal constructor(
        override val offset: Int,
        override val length: Int,
        /** Programming language of the entity text. */
        val language: String? = null
    ) : MessageEntity {
        override val type: Type = Type.PRE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is clickable text linked to [url]. */
    data class TextLink internal constructor(
        override val offset: Int,
        override val length: Int,
        /** URL opened after the user taps the text. */
        val url: String
    ) : MessageEntity {
        override val type: Type = Type.TEXT_LINK
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a mention of a user without a username. */
    data class TextMention internal constructor(
        override val offset: Int,
        override val length: Int,
        /** Mentioned user. */
        val user: User
    ) : MessageEntity {
        override val type: Type = Type.TEXT_MENTION
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a custom emoji. */
    data class CustomEmoji internal constructor(
        override val offset: Int,
        override val length: Int,
        /** Unique identifier of the custom emoji. */
        val customEmojiId: String
    ) : MessageEntity {
        override val type: Type = Type.CUSTOM_EMOJI
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a date or time. */
    data class DateTime internal constructor(
        override val offset: Int,
        override val length: Int,
        /** Unix time represented by the entity. */
        val unixTime: Instant? = null,
        /** Format of the date and time. */
        val dateTimeFormat: String? = null
    ) : MessageEntity {
        override val type: Type = Type.DATE_TIME
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is a block quotation. */
    data class BlockQuote internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BLOCKQUOTE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /** Case when the entity is an expandable block quotation. */
    data class ExpandableBlockQuote internal constructor(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.EXPANDABLE_BLOCKQUOTE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    /**
     * Value for [type]
     */
    enum class Type {
        @JsonProperty(MENTION_STR)
        MENTION,
        @JsonProperty(HASHTAG_STR)
        HASHTAG,
        @JsonProperty(CASHTAG_STR)
        CASHTAG,
        @JsonProperty(BOT_COMMAND_STR)
        BOT_COMMAND,
        @JsonProperty(URL_STR)
        URL,
        @JsonProperty(EMAIL_STR)
        EMAIL,
        @JsonProperty(PHONE_NUMBER_STR)
        PHONE_NUMBER,
        @JsonProperty(BOLD_STR)
        BOLD,
        @JsonProperty(ITALIC_STR)
        ITALIC,
        @JsonProperty(UNDERLINE_STR)
        UNDERLINE,
        @JsonProperty(STRIKETHROUGH_STR)
        STRIKETHROUGH,
        @JsonProperty(SPOILER_STR)
        SPOILER,
        @JsonProperty(CODE_STR)
        CODE,
        @JsonProperty(PRE_STR)
        PRE,
        @JsonProperty(TEXT_LINK_STR)
        TEXT_LINK,
        @JsonProperty(TEXT_MENTION_STR)
        TEXT_MENTION,
        @JsonProperty(CUSTOM_EMOJI_STR)
        CUSTOM_EMOJI,
        @JsonProperty(DATE_TIME_STR)
        DATE_TIME,
        @JsonProperty(BLOCKQUOTE_STR)
        BLOCKQUOTE,
        @JsonProperty(EXPANDABLE_BLOCKQUOTE_STR)
        EXPANDABLE_BLOCKQUOTE;

        companion object {
            const val MENTION_STR = "mention"
            const val HASHTAG_STR = "hashtag"
            const val CASHTAG_STR = "cashtag"
            const val BOT_COMMAND_STR = "bot_command"
            const val URL_STR = "url"
            const val EMAIL_STR = "email"
            const val PHONE_NUMBER_STR = "phone_number"
            const val BOLD_STR = "bold"
            const val ITALIC_STR = "italic"
            const val UNDERLINE_STR = "underline"
            const val STRIKETHROUGH_STR = "strikethrough"
            const val SPOILER_STR = "spoiler"
            const val CODE_STR = "code"
            const val PRE_STR = "pre"
            const val TEXT_LINK_STR = "text_link"
            const val TEXT_MENTION_STR = "text_mention"
            const val CUSTOM_EMOJI_STR = "custom_emoji"
            const val DATE_TIME_STR = "date_time"
            const val BLOCKQUOTE_STR = "blockquote"
            const val EXPANDABLE_BLOCKQUOTE_STR = "expandable_blockquote"
        }
    }
}
