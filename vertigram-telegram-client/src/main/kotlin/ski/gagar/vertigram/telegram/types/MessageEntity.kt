package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Telegram [MessageEntity](https://core.telegram.org/bots/api#messageentity) type.
 *
 * Subtypes (which are nested) represent the case for each of [MessageEntity.Type].
 * Most of the subtypes are effectively the same (type+offset+length),
 * which means you can access the fields using the [MessageEntity] interface.
 *
 * For up-to-date documentation please consult the official Telegram docs.
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
    JsonSubTypes.Type(value = MessageEntity.BlockQuote::class, name = MessageEntity.Type.BLOCKQUOTE_STR),
)
sealed interface MessageEntity {
    val type: Type
    val offset: Int
    val length: Int

    fun copyTo(offset: Int = this.offset, length: Int = this.length): MessageEntity

    data class Mention(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.MENTION
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Hashtag(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.HASHTAG
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Cashtag(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.CASHTAG
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class BotCommand(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BOT_COMMAND
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Url(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.URL
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Email(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.EMAIL
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class PhoneNumber(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.PHONE_NUMBER
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Bold(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BOLD
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Italic(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.ITALIC
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Underline(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.UNDERLINE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Strikethrough(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.STRIKETHROUGH
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Spoiler(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.SPOILER
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Code(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.CODE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class Pre(
        override val offset: Int,
        override val length: Int,
        val language: String? = null
    ) : MessageEntity {
        override val type: Type = Type.PRE
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class TextLink(
        override val offset: Int,
        override val length: Int,
        val url: String
    ) : MessageEntity {
        override val type: Type = Type.TEXT_LINK
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class TextMention(
        override val offset: Int,
        override val length: Int,
        val user: User
    ) : MessageEntity {
        override val type: Type = Type.TEXT_MENTION
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class CustomEmoji(
        override val offset: Int,
        override val length: Int,
        val customEmojiId: String
    ) : MessageEntity {
        override val type: Type = Type.CUSTOM_EMOJI
        override fun copyTo(offset: Int, length: Int) = copy(offset = offset, length = length)
    }

    data class BlockQuote(
        override val offset: Int,
        override val length: Int
    ) : MessageEntity {
        override val type: Type = Type.BLOCKQUOTE
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
        @JsonProperty(BLOCKQUOTE_STR)
        BLOCKQUOTE;

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
            const val BLOCKQUOTE_STR = "blockquote"
        }
    }
}