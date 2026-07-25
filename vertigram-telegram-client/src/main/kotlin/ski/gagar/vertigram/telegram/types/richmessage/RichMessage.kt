package ski.gagar.vertigram.telegram.types.richmessage

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Animation
import ski.gagar.vertigram.telegram.types.Location
import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.PhotoSize
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.Voice
import java.time.Instant

/**
 * Represents a rich formatted message.
 *
 * See Telegram's [RichMessage](https://core.telegram.org/bots/api#richmessage) documentation.
 */
@TelegramCodegen.Type
data class RichMessage internal constructor(
    /** Content of the message described as a list of blocks. */
    val blocks: List<Block>,
    /** Whether the rich message must be shown right-to-left. */
    @get:JvmName("getIsRtl")
    val isRtl: Boolean = false
) {
    companion object
}

/**
 * Represents a rich formatted text object.
 *
 * Telegram can also return plain strings and arrays as rich text values. Vertigram represents all three forms with
 * [RichTextValue].
 *
 * See Telegram's [RichText](https://core.telegram.org/bots/api#richtext) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = RichText.Bold::class, name = RichText.Type.BOLD_STR),
    JsonSubTypes.Type(value = RichText.Italic::class, name = RichText.Type.ITALIC_STR),
    JsonSubTypes.Type(value = RichText.Underline::class, name = RichText.Type.UNDERLINE_STR),
    JsonSubTypes.Type(value = RichText.Strikethrough::class, name = RichText.Type.STRIKETHROUGH_STR),
    JsonSubTypes.Type(value = RichText.Spoiler::class, name = RichText.Type.SPOILER_STR),
    JsonSubTypes.Type(value = RichText.DateTime::class, name = RichText.Type.DATE_TIME_STR),
    JsonSubTypes.Type(value = RichText.TextMention::class, name = RichText.Type.TEXT_MENTION_STR),
    JsonSubTypes.Type(value = RichText.Subscript::class, name = RichText.Type.SUBSCRIPT_STR),
    JsonSubTypes.Type(value = RichText.Superscript::class, name = RichText.Type.SUPERSCRIPT_STR),
    JsonSubTypes.Type(value = RichText.Marked::class, name = RichText.Type.MARKED_STR),
    JsonSubTypes.Type(value = RichText.Code::class, name = RichText.Type.CODE_STR),
    JsonSubTypes.Type(value = RichText.CustomEmoji::class, name = RichText.Type.CUSTOM_EMOJI_STR),
    JsonSubTypes.Type(value = RichText.MathematicalExpression::class, name = RichText.Type.MATHEMATICAL_EXPRESSION_STR),
    JsonSubTypes.Type(value = RichText.Url::class, name = RichText.Type.URL_STR),
    JsonSubTypes.Type(value = RichText.EmailAddress::class, name = RichText.Type.EMAIL_ADDRESS_STR),
    JsonSubTypes.Type(value = RichText.PhoneNumber::class, name = RichText.Type.PHONE_NUMBER_STR),
    JsonSubTypes.Type(value = RichText.BankCardNumber::class, name = RichText.Type.BANK_CARD_NUMBER_STR),
    JsonSubTypes.Type(value = RichText.Mention::class, name = RichText.Type.MENTION_STR),
    JsonSubTypes.Type(value = RichText.Hashtag::class, name = RichText.Type.HASHTAG_STR),
    JsonSubTypes.Type(value = RichText.Cashtag::class, name = RichText.Type.CASHTAG_STR),
    JsonSubTypes.Type(value = RichText.BotCommandText::class, name = RichText.Type.BOT_COMMAND_STR),
    JsonSubTypes.Type(value = RichText.Anchor::class, name = RichText.Type.ANCHOR_STR),
    JsonSubTypes.Type(value = RichText.AnchorLink::class, name = RichText.Type.ANCHOR_LINK_STR),
    JsonSubTypes.Type(value = RichText.Reference::class, name = RichText.Type.REFERENCE_STR),
    JsonSubTypes.Type(value = RichText.ReferenceLink::class, name = RichText.Type.REFERENCE_LINK_STR)
)
sealed interface RichText {
    /** Type of the rich formatted text. */
    val type: Type

    /**
     * Case for a bold text.
     *
     * See Telegram's [RichTextBold](https://core.telegram.org/bots/api#richtextbold) documentation.
     */
    @TelegramCodegen.Type
    data class Bold internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.BOLD
        companion object
    }

    /**
     * Case for an italicized text.
     *
     * See Telegram's [RichTextItalic](https://core.telegram.org/bots/api#richtextitalic) documentation.
     */
    @TelegramCodegen.Type
    data class Italic internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.ITALIC
        companion object
    }

    /**
     * Case for an underlined text.
     *
     * See Telegram's [RichTextUnderline](https://core.telegram.org/bots/api#richtextunderline) documentation.
     */
    @TelegramCodegen.Type
    data class Underline internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.UNDERLINE
        companion object
    }

    /**
     * Case for a strikethrough text.
     *
     * See Telegram's
     * [RichTextStrikethrough](https://core.telegram.org/bots/api#richtextstrikethrough) documentation.
     */
    @TelegramCodegen.Type
    data class Strikethrough internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.STRIKETHROUGH
        companion object
    }

    /**
     * Case for a text covered by a spoiler.
     *
     * See Telegram's [RichTextSpoiler](https://core.telegram.org/bots/api#richtextspoiler) documentation.
     */
    @TelegramCodegen.Type
    data class Spoiler internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.SPOILER
        companion object
    }

    /**
     * Case for formatted date and time.
     *
     * See Telegram's [RichTextDateTime](https://core.telegram.org/bots/api#richtextdatetime) documentation.
     */
    @TelegramCodegen.Type
    data class DateTime internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Unix time associated with the entity. */
        val unixTime: Instant,
        /**
         * String that defines the formatting of the date and time.
         *
         * See Telegram's
         * [date-time entity formatting](https://core.telegram.org/bots/api#date-time-entity-formatting) documentation.
         */
        val dateTimeFormat: String
    ) : RichText {
        override val type = Type.DATE_TIME
        companion object
    }

    /**
     * Case for a mention of a Telegram user by their identifier.
     *
     * See Telegram's [RichTextTextMention](https://core.telegram.org/bots/api#richtexttextmention) documentation.
     */
    @TelegramCodegen.Type
    data class TextMention internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Mentioned user. */
        val user: User
    ) : RichText {
        override val type = Type.TEXT_MENTION
        companion object
    }

    /**
     * Case for a subscript text.
     *
     * See Telegram's [RichTextSubscript](https://core.telegram.org/bots/api#richtextsubscript) documentation.
     */
    @TelegramCodegen.Type
    data class Subscript internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.SUBSCRIPT
        companion object
    }

    /**
     * Case for a superscript text.
     *
     * See Telegram's [RichTextSuperscript](https://core.telegram.org/bots/api#richtextsuperscript) documentation.
     */
    @TelegramCodegen.Type
    data class Superscript internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.SUPERSCRIPT
        companion object
    }

    /**
     * Case for a marked text.
     *
     * See Telegram's [RichTextMarked](https://core.telegram.org/bots/api#richtextmarked) documentation.
     */
    @TelegramCodegen.Type
    data class Marked internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.MARKED
        companion object
    }

    /**
     * Case for a monowidth text.
     *
     * See Telegram's [RichTextCode](https://core.telegram.org/bots/api#richtextcode) documentation.
     */
    @TelegramCodegen.Type
    data class Code internal constructor(
        /** The text. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.CODE
        companion object
    }

    /**
     * Case for a custom emoji.
     *
     * See Telegram's [RichTextCustomEmoji](https://core.telegram.org/bots/api#richtextcustomemoji) documentation.
     */
    @TelegramCodegen.Type
    data class CustomEmoji internal constructor(
        /**
         * Unique identifier of the custom emoji.
         *
         * Use [getCustomEmojiStickers][ski.gagar.vertigram.telegram.methods.getCustomEmojiStickers] to get full
         * information about the sticker.
         */
        val customEmojiId: String,
        /** Alternative emoji for the custom emoji. */
        val alternativeText: String
    ) : RichText {
        override val type = Type.CUSTOM_EMOJI
        companion object
    }

    /**
     * Case for a mathematical expression.
     *
     * See Telegram's
     * [RichTextMathematicalExpression](https://core.telegram.org/bots/api#richtextmathematicalexpression)
     * documentation.
     */
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(
        /** Expression in LaTeX format. */
        val expression: String
    ) : RichText {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }

    /**
     * Case for a text with a link.
     *
     * See Telegram's [RichTextUrl](https://core.telegram.org/bots/api#richtexturl) documentation.
     */
    @TelegramCodegen.Type
    data class Url internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** URL of the link. */
        val url: String
    ) : RichText {
        override val type = Type.URL
        companion object
    }

    /**
     * Case for a text with an email address.
     *
     * See Telegram's [RichTextEmailAddress](https://core.telegram.org/bots/api#richtextemailaddress) documentation.
     */
    @TelegramCodegen.Type
    data class EmailAddress internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Email address. */
        val emailAddress: String
    ) : RichText {
        override val type = Type.EMAIL_ADDRESS
        companion object
    }

    /**
     * Case for a text with a phone number.
     *
     * See Telegram's [RichTextPhoneNumber](https://core.telegram.org/bots/api#richtextphonenumber) documentation.
     */
    @TelegramCodegen.Type
    data class PhoneNumber internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Phone number. */
        val phoneNumber: String
    ) : RichText {
        override val type = Type.PHONE_NUMBER
        companion object
    }

    /**
     * Case for a text with a bank card number.
     *
     * See Telegram's
     * [RichTextBankCardNumber](https://core.telegram.org/bots/api#richtextbankcardnumber) documentation.
     */
    @TelegramCodegen.Type
    data class BankCardNumber internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Bank card number. */
        val bankCardNumber: String
    ) : RichText {
        override val type = Type.BANK_CARD_NUMBER
        companion object
    }

    /**
     * Case for a mention by a username.
     *
     * See Telegram's [RichTextMention](https://core.telegram.org/bots/api#richtextmention) documentation.
     */
    @TelegramCodegen.Type
    data class Mention internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Username. */
        val username: String
    ) : RichText {
        override val type = Type.MENTION
        companion object
    }

    /**
     * Case for a hashtag.
     *
     * See Telegram's [RichTextHashtag](https://core.telegram.org/bots/api#richtexthashtag) documentation.
     */
    @TelegramCodegen.Type
    data class Hashtag internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Hashtag. */
        val hashtag: String
    ) : RichText {
        override val type = Type.HASHTAG
        companion object
    }

    /**
     * Case for a cashtag.
     *
     * See Telegram's [RichTextCashtag](https://core.telegram.org/bots/api#richtextcashtag) documentation.
     */
    @TelegramCodegen.Type
    data class Cashtag internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Cashtag. */
        val cashtag: String
    ) : RichText {
        override val type = Type.CASHTAG
        companion object
    }

    /**
     * Case for a bot command.
     *
     * See Telegram's [RichTextBotCommand](https://core.telegram.org/bots/api#richtextbotcommand) documentation.
     */
    @TelegramCodegen.Type
    data class BotCommandText internal constructor(
        /** The text. */
        val text: RichTextValue,
        /** Bot command. */
        val botCommand: String
    ) : RichText {
        override val type = Type.BOT_COMMAND
        companion object
    }

    /**
     * Case for an anchor.
     *
     * See Telegram's [RichTextAnchor](https://core.telegram.org/bots/api#richtextanchor) documentation.
     */
    @TelegramCodegen.Type
    data class Anchor internal constructor(
        /** Name of the anchor. */
        val name: String
    ) : RichText {
        override val type = Type.ANCHOR
        companion object
    }

    /**
     * Case for a link to an anchor.
     *
     * See Telegram's [RichTextAnchorLink](https://core.telegram.org/bots/api#richtextanchorlink) documentation.
     */
    @TelegramCodegen.Type
    data class AnchorLink internal constructor(
        /** Link text. */
        val text: RichTextValue,
        /** Name of the anchor. An empty name makes the link return to the top of the message. */
        val anchorName: String
    ) : RichText {
        override val type = Type.ANCHOR_LINK
        companion object
    }

    /**
     * Case for a reference.
     *
     * See Telegram's [RichTextReference](https://core.telegram.org/bots/api#richtextreference) documentation.
     */
    @TelegramCodegen.Type
    data class Reference internal constructor(
        /** Name of the reference. */
        val name: String,
        /** Text of the reference. */
        val text: RichTextValue
    ) : RichText {
        override val type = Type.REFERENCE
        companion object
    }

    /**
     * Case for a link to a reference.
     *
     * See Telegram's [RichTextReferenceLink](https://core.telegram.org/bots/api#richtextreferencelink) documentation.
     */
    @TelegramCodegen.Type
    data class ReferenceLink internal constructor(
        /** Link text. */
        val text: RichTextValue,
        /** Name of the reference. */
        val referenceName: String
    ) : RichText {
        override val type = Type.REFERENCE_LINK
        companion object
    }

    /** Type of a rich formatted text object. */
    enum class Type {
        /** Case for a bold text. */
        @JsonProperty(BOLD_STR) BOLD,
        /** Case for an italicized text. */
        @JsonProperty(ITALIC_STR) ITALIC,
        /** Case for an underlined text. */
        @JsonProperty(UNDERLINE_STR) UNDERLINE,
        /** Case for a strikethrough text. */
        @JsonProperty(STRIKETHROUGH_STR) STRIKETHROUGH,
        /** Case for a text covered by a spoiler. */
        @JsonProperty(SPOILER_STR) SPOILER,
        /** Case for formatted date and time. */
        @JsonProperty(DATE_TIME_STR) DATE_TIME,
        /** Case for a mention of a Telegram user by their identifier. */
        @JsonProperty(TEXT_MENTION_STR) TEXT_MENTION,
        /** Case for a subscript text. */
        @JsonProperty(SUBSCRIPT_STR) SUBSCRIPT,
        /** Case for a superscript text. */
        @JsonProperty(SUPERSCRIPT_STR) SUPERSCRIPT,
        /** Case for a marked text. */
        @JsonProperty(MARKED_STR) MARKED,
        /** Case for a monowidth text. */
        @JsonProperty(CODE_STR) CODE,
        /** Case for a custom emoji. */
        @JsonProperty(CUSTOM_EMOJI_STR) CUSTOM_EMOJI,
        /** Case for a mathematical expression. */
        @JsonProperty(MATHEMATICAL_EXPRESSION_STR) MATHEMATICAL_EXPRESSION,
        /** Case for a text with a link. */
        @JsonProperty(URL_STR) URL,
        /** Case for a text with an email address. */
        @JsonProperty(EMAIL_ADDRESS_STR) EMAIL_ADDRESS,
        /** Case for a text with a phone number. */
        @JsonProperty(PHONE_NUMBER_STR) PHONE_NUMBER,
        /** Case for a text with a bank card number. */
        @JsonProperty(BANK_CARD_NUMBER_STR) BANK_CARD_NUMBER,
        /** Case for a mention by a username. */
        @JsonProperty(MENTION_STR) MENTION,
        /** Case for a hashtag. */
        @JsonProperty(HASHTAG_STR) HASHTAG,
        /** Case for a cashtag. */
        @JsonProperty(CASHTAG_STR) CASHTAG,
        /** Case for a bot command. */
        @JsonProperty(BOT_COMMAND_STR) BOT_COMMAND,
        /** Case for an anchor. */
        @JsonProperty(ANCHOR_STR) ANCHOR,
        /** Case for a link to an anchor. */
        @JsonProperty(ANCHOR_LINK_STR) ANCHOR_LINK,
        /** Case for a reference. */
        @JsonProperty(REFERENCE_STR) REFERENCE,
        /** Case for a link to a reference. */
        @JsonProperty(REFERENCE_LINK_STR) REFERENCE_LINK;

        companion object {
            const val BOLD_STR = "bold"
            const val ITALIC_STR = "italic"
            const val UNDERLINE_STR = "underline"
            const val STRIKETHROUGH_STR = "strikethrough"
            const val SPOILER_STR = "spoiler"
            const val DATE_TIME_STR = "date_time"
            const val TEXT_MENTION_STR = "text_mention"
            const val SUBSCRIPT_STR = "subscript"
            const val SUPERSCRIPT_STR = "superscript"
            const val MARKED_STR = "marked"
            const val CODE_STR = "code"
            const val CUSTOM_EMOJI_STR = "custom_emoji"
            const val MATHEMATICAL_EXPRESSION_STR = "mathematical_expression"
            const val URL_STR = "url"
            const val EMAIL_ADDRESS_STR = "email_address"
            const val PHONE_NUMBER_STR = "phone_number"
            const val BANK_CARD_NUMBER_STR = "bank_card_number"
            const val MENTION_STR = "mention"
            const val HASHTAG_STR = "hashtag"
            const val CASHTAG_STR = "cashtag"
            const val BOT_COMMAND_STR = "bot_command"
            const val ANCHOR_STR = "anchor"
            const val ANCHOR_LINK_STR = "anchor_link"
            const val REFERENCE_STR = "reference"
            const val REFERENCE_LINK_STR = "reference_link"
        }
    }
}

/**
 * A rich formatted text represented as a plain string, an array of rich text values, or a [RichText] object.
 *
 * See Telegram's [RichText](https://core.telegram.org/bots/api#richtext) documentation.
 */
@JsonSerialize(using = RichTextValue.Serializer::class)
@JsonDeserialize(using = RichTextValue.Deserializer::class)
sealed interface RichTextValue {
    /** Case when the rich formatted text is a plain string. */
    data class Plain internal constructor(
        /** Plain text. */
        val value: String
    ) : RichTextValue

    /** Case when the rich formatted text is an array of rich text values. */
    data class Parts internal constructor(
        /** Parts of the rich formatted text. */
        val values: List<RichTextValue>
    ) : RichTextValue

    /** Case when the rich formatted text is a [RichText] object. */
    data class Formatted internal constructor(
        /** Rich formatted text object. */
        val value: RichText
    ) : RichTextValue

    /** Serializes the three representations of [RichTextValue] accepted by Telegram. */
    class Serializer : JsonSerializer<RichTextValue>() {
        override fun serialize(value: RichTextValue, gen: JsonGenerator, serializers: SerializerProvider) {
            when (value) {
                is Plain -> gen.writeString(value.value)
                is Parts -> {
                    gen.writeStartArray()
                    for (part in value.values) {
                        serializers.defaultSerializeValue(part, gen)
                    }
                    gen.writeEndArray()
                }
                is Formatted -> serializers.defaultSerializeValue(value.value, gen)
            }
        }
    }

    /** Deserializes the three representations of [RichTextValue] returned by Telegram. */
    class Deserializer : JsonDeserializer<RichTextValue>() {
        override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): RichTextValue {
            val codec = parser.codec
            val node = codec.readTree<JsonNode>(parser)
            return when {
                node.isTextual -> Plain(node.asText())
                node.isArray -> Parts(node.map { codec.treeToValue(it, RichTextValue::class.java) })
                node.isObject -> Formatted(codec.treeToValue(node, RichText::class.java))
                else -> ctxt.reportInputMismatch(
                    RichTextValue::class.java,
                    "Expected rich text string, array, or object"
                )
            }
        }
    }

    companion object {
        /** Creates a rich text value represented as a plain string. */
        fun plain(value: String): RichTextValue = Plain(value)

        /** Creates a rich text value represented as an array of [values]. */
        fun parts(values: List<RichTextValue>): RichTextValue = Parts(values)

        /** Creates a rich text value represented as a [RichText] object. */
        fun formatted(value: RichText): RichTextValue = Formatted(value)
    }
}

/**
 * Caption of a rich formatted block.
 *
 * See Telegram's [RichBlockCaption](https://core.telegram.org/bots/api#richblockcaption) documentation.
 */
@TelegramCodegen.Type
data class Caption internal constructor(
    /** Block caption. */
    val text: RichTextValue,
    /** Block credit corresponding to the HTML tag `<cite>`. */
    val credit: RichTextValue? = null
) {
    companion object
}

/**
 * Cell in a table.
 *
 * See Telegram's [RichBlockTableCell](https://core.telegram.org/bots/api#richblocktablecell) documentation.
 */
@TelegramCodegen.Type
data class TableCell internal constructor(
    /** Text in the cell. */
    val text: RichTextValue,
    /** Whether the cell is a header cell. */
    @get:JvmName("getIsHeader")
    val isHeader: Boolean = false,
    /** Number of columns the cell spans if it is bigger than 1. */
    val colspan: Int? = null,
    /** Number of rows the cell spans if it is bigger than 1. */
    val rowspan: Int? = null,
    /** Horizontal cell content alignment. */
    val align: Align? = null,
    /** Vertical cell content alignment. */
    val verticalAlign: VerticalAlign? = null
) {
    /** Horizontal cell content alignment. */
    enum class Align {
        /** Case for left-aligned content. */
        @JsonProperty("left") LEFT,
        /** Case for centered content. */
        @JsonProperty("center") CENTER,
        /** Case for right-aligned content. */
        @JsonProperty("right") RIGHT
    }

    /** Vertical cell content alignment. */
    enum class VerticalAlign {
        /** Case for top-aligned content. */
        @JsonProperty("top") TOP,
        /** Case for vertically centered content. */
        @JsonProperty("middle") MIDDLE,
        /** Case for bottom-aligned content. */
        @JsonProperty("bottom") BOTTOM
    }
    companion object
}

/**
 * Item of a list.
 *
 * See Telegram's [RichBlockListItem](https://core.telegram.org/bots/api#richblocklistitem) documentation.
 */
@TelegramCodegen.Type
data class ListItem internal constructor(
    /** Content of the item. */
    val blocks: List<Block>,
    /** Label of the item. */
    val number: String? = null,
    /** Whether the item has a checked checkbox; `null` when the item has no checkbox. */
    val checked: Boolean? = null
) {
    companion object
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Block.Paragraph::class, name = Block.Type.PARAGRAPH_STR),
    JsonSubTypes.Type(value = Block.SectionHeading::class, name = Block.Type.HEADING_STR),
    JsonSubTypes.Type(value = Block.Preformatted::class, name = Block.Type.PRE_STR),
    JsonSubTypes.Type(value = Block.Footer::class, name = Block.Type.FOOTER_STR),
    JsonSubTypes.Type(value = Block.Divider::class, name = Block.Type.DIVIDER_STR),
    JsonSubTypes.Type(value = Block.MathematicalExpression::class, name = Block.Type.MATHEMATICAL_EXPRESSION_STR),
    JsonSubTypes.Type(value = Block.Anchor::class, name = Block.Type.ANCHOR_STR),
    JsonSubTypes.Type(value = Block.List::class, name = Block.Type.LIST_STR),
    JsonSubTypes.Type(value = Block.BlockQuotation::class, name = Block.Type.BLOCKQUOTE_STR),
    JsonSubTypes.Type(value = Block.PullQuotation::class, name = Block.Type.PULLQUOTE_STR),
    JsonSubTypes.Type(value = Block.Collage::class, name = Block.Type.COLLAGE_STR),
    JsonSubTypes.Type(value = Block.Slideshow::class, name = Block.Type.SLIDESHOW_STR),
    JsonSubTypes.Type(value = Block.Table::class, name = Block.Type.TABLE_STR),
    JsonSubTypes.Type(value = Block.Details::class, name = Block.Type.DETAILS_STR),
    JsonSubTypes.Type(value = Block.Map::class, name = Block.Type.MAP_STR),
    JsonSubTypes.Type(value = Block.Animation::class, name = Block.Type.ANIMATION_STR),
    JsonSubTypes.Type(value = Block.Audio::class, name = Block.Type.AUDIO_STR),
    JsonSubTypes.Type(value = Block.Photo::class, name = Block.Type.PHOTO_STR),
    JsonSubTypes.Type(value = Block.Video::class, name = Block.Type.VIDEO_STR),
    JsonSubTypes.Type(value = Block.VoiceNote::class, name = Block.Type.VOICE_NOTE_STR),
    JsonSubTypes.Type(value = Block.Thinking::class, name = Block.Type.THINKING_STR)
)
/**
 * Represents a block in a rich formatted message.
 *
 * See Telegram's [RichBlock](https://core.telegram.org/bots/api#richblock) documentation.
 */
sealed interface Block {
    /** Type of the block. */
    val type: Type

    /**
     * Case for a text paragraph corresponding to the HTML tag `<p>`.
     *
     * See Telegram's [RichBlockParagraph](https://core.telegram.org/bots/api#richblockparagraph) documentation.
     */
    @TelegramCodegen.Type
    data class Paragraph internal constructor(
        /** Text of the block. */
        val text: RichTextValue
    ) : Block {
        override val type = Type.PARAGRAPH
        companion object
    }

    /**
     * Case for a section heading corresponding to the HTML tags `<h1>` through `<h6>`.
     *
     * See Telegram's
     * [RichBlockSectionHeading](https://core.telegram.org/bots/api#richblocksectionheading) documentation.
     */
    @TelegramCodegen.Type
    data class SectionHeading internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Relative size of the text font; 1-6, where 1 is the largest and 6 is the smallest. */
        val size: Int
    ) : Block {
        override val type = Type.HEADING
        companion object
    }

    /**
     * Case for a preformatted text block corresponding to the nested HTML tags `<pre>` and `<code>`.
     *
     * See Telegram's [RichBlockPreformatted](https://core.telegram.org/bots/api#richblockpreformatted) documentation.
     */
    @TelegramCodegen.Type
    data class Preformatted internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Programming language of the text. */
        val language: String? = null
    ) : Block {
        override val type = Type.PRE
        companion object
    }

    /**
     * Case for a footer corresponding to the HTML tag `<footer>`.
     *
     * See Telegram's [RichBlockFooter](https://core.telegram.org/bots/api#richblockfooter) documentation.
     */
    @TelegramCodegen.Type
    data class Footer internal constructor(
        /** Text of the block. */
        val text: RichTextValue
    ) : Block {
        override val type = Type.FOOTER
        companion object
    }

    /**
     * Case for a divider corresponding to the HTML tag `<hr/>`.
     *
     * See Telegram's [RichBlockDivider](https://core.telegram.org/bots/api#richblockdivider) documentation.
     */
    data object Divider : Block {
        override val type = Type.DIVIDER
    }

    /**
     * Case for a mathematical expression in LaTeX format corresponding to the custom HTML tag `<tg-math-block>`.
     *
     * See Telegram's
     * [RichBlockMathematicalExpression](https://core.telegram.org/bots/api#richblockmathematicalexpression)
     * documentation.
     */
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(
        /** Mathematical expression in LaTeX format. */
        val expression: String
    ) : Block {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }

    /**
     * Case for an anchor corresponding to the HTML tag `<a>` with the attribute `name`.
     *
     * See Telegram's [RichBlockAnchor](https://core.telegram.org/bots/api#richblockanchor) documentation.
     */
    @TelegramCodegen.Type
    data class Anchor internal constructor(
        /** Name of the anchor. */
        val name: String
    ) : Block {
        override val type = Type.ANCHOR
        companion object
    }

    /**
     * Case for a list of blocks corresponding to the HTML tag `<ul>` or `<ol>` with nested `<li>` tags.
     *
     * See Telegram's [RichBlockList](https://core.telegram.org/bots/api#richblocklist) documentation.
     */
    @TelegramCodegen.Type
    data class List internal constructor(
        /** Items of the list. */
        val items: kotlin.collections.List<ListItem>
    ) : Block {
        override val type = Type.LIST
        companion object
    }

    /**
     * Case for a block quotation corresponding to the HTML tag `<blockquote>`.
     *
     * See Telegram's
     * [RichBlockBlockQuotation](https://core.telegram.org/bots/api#richblockblockquotation) documentation.
     */
    @TelegramCodegen.Type
    data class BlockQuotation internal constructor(
        /** Content of the block. */
        val blocks: kotlin.collections.List<Block>,
        /** Credit of the block. */
        val credit: RichTextValue? = null
    ) : Block {
        override val type = Type.BLOCKQUOTE
        companion object
    }

    /**
     * Case for a quotation with centered text, loosely corresponding to the HTML tag `<aside>`.
     *
     * See Telegram's
     * [RichBlockPullQuotation](https://core.telegram.org/bots/api#richblockpullquotation) documentation.
     */
    @TelegramCodegen.Type
    data class PullQuotation internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Credit of the block. */
        val credit: RichTextValue? = null
    ) : Block {
        override val type = Type.PULLQUOTE
        companion object
    }

    /**
     * Case for a collage corresponding to the custom HTML tag `<tg-collage>`.
     *
     * See Telegram's [RichBlockCollage](https://core.telegram.org/bots/api#richblockcollage) documentation.
     */
    @TelegramCodegen.Type
    data class Collage internal constructor(
        /** Elements of the collage. */
        val blocks: kotlin.collections.List<Block>,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.COLLAGE
        companion object
    }

    /**
     * Case for a slideshow corresponding to the custom HTML tag `<tg-slideshow>`.
     *
     * See Telegram's [RichBlockSlideshow](https://core.telegram.org/bots/api#richblockslideshow) documentation.
     */
    @TelegramCodegen.Type
    data class Slideshow internal constructor(
        /** Elements of the slideshow. */
        val blocks: kotlin.collections.List<Block>,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.SLIDESHOW
        companion object
    }

    /**
     * Case for a table corresponding to the HTML tag `<table>`.
     *
     * See Telegram's [RichBlockTable](https://core.telegram.org/bots/api#richblocktable) documentation.
     */
    @TelegramCodegen.Type
    data class Table internal constructor(
        /** Cells of the table. */
        val cells: kotlin.collections.List<kotlin.collections.List<TableCell>>,
        /** Whether the table has borders. */
        @get:JvmName("getIsBordered")
        val isBordered: Boolean = false,
        /** Whether the table is striped. */
        @get:JvmName("getIsStriped")
        val isStriped: Boolean = false,
        /** Caption of the table. */
        val caption: RichTextValue? = null
    ) : Block {
        override val type = Type.TABLE
        companion object
    }

    /**
     * Case for an expandable details disclosure block corresponding to the HTML tag `<details>`.
     *
     * See Telegram's [RichBlockDetails](https://core.telegram.org/bots/api#richblockdetails) documentation.
     */
    @TelegramCodegen.Type
    data class Details internal constructor(
        /** Always shown summary of the block. */
        val summary: RichTextValue,
        /** Content of the block. */
        val blocks: kotlin.collections.List<Block>,
        /** Whether the content of the block is visible initially. */
        @get:JvmName("getIsOpen")
        val isOpen: Boolean = false
    ) : Block {
        override val type = Type.DETAILS
        companion object
    }

    /**
     * Case for a block with a map corresponding to the custom HTML tag `<tg-map>`.
     *
     * See Telegram's [RichBlockMap](https://core.telegram.org/bots/api#richblockmap) documentation.
     */
    @TelegramCodegen.Type
    data class Map internal constructor(
        /** Location of the center of the map. */
        val location: Location,
        /** Map zoom level; 13-20. */
        val zoom: Int,
        /** Expected width of the map. */
        val width: Int,
        /** Expected height of the map. */
        val height: Int,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.MAP
        companion object
    }

    /**
     * Case for a block with an animation corresponding to the HTML tag `<video>`.
     *
     * See Telegram's [RichBlockAnimation](https://core.telegram.org/bots/api#richblockanimation) documentation.
     */
    @TelegramCodegen.Type
    data class Animation internal constructor(
        /** Animation. */
        val animation: ski.gagar.vertigram.telegram.types.Animation,
        /** Whether the media preview is covered by a spoiler animation. */
        val hasSpoiler: Boolean = false,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.ANIMATION
        companion object
    }

    /**
     * Case for a block with a music file corresponding to the HTML tag `<audio>`.
     *
     * See Telegram's [RichBlockAudio](https://core.telegram.org/bots/api#richblockaudio) documentation.
     */
    @TelegramCodegen.Type
    data class Audio internal constructor(
        /** Audio. */
        val audio: ski.gagar.vertigram.telegram.types.Audio,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.AUDIO
        companion object
    }

    /**
     * Case for a block with a photo corresponding to the HTML tag `<img>`.
     *
     * See Telegram's [RichBlockPhoto](https://core.telegram.org/bots/api#richblockphoto) documentation.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        /** Available sizes of the photo. */
        val photo: kotlin.collections.List<PhotoSize>,
        /** Whether the media preview is covered by a spoiler animation. */
        val hasSpoiler: Boolean = false,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.PHOTO
        companion object
    }

    /**
     * Case for a block with a video corresponding to the HTML tag `<video>`.
     *
     * See Telegram's [RichBlockVideo](https://core.telegram.org/bots/api#richblockvideo) documentation.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
        /** Video. */
        val video: ski.gagar.vertigram.telegram.types.Video,
        /** Whether the media preview is covered by a spoiler animation. */
        val hasSpoiler: Boolean = false,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.VIDEO
        companion object
    }

    /**
     * Case for a block with a voice note corresponding to the HTML tag `<audio>`.
     *
     * See Telegram's [RichBlockVoiceNote](https://core.telegram.org/bots/api#richblockvoicenote) documentation.
     */
    @TelegramCodegen.Type
    data class VoiceNote internal constructor(
        /** Voice note. */
        val voiceNote: Voice,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : Block {
        override val type = Type.VOICE_NOTE
        companion object
    }

    /**
     * Case for a block with a “Thinking…” placeholder corresponding to the custom HTML tag `<tg-thinking>`.
     *
     * The block may be used only in
     * [sendRichMessageDraft][ski.gagar.vertigram.telegram.methods.sendRichMessageDraft] and therefore can't be
     * received in messages.
     *
     * See Telegram's [RichBlockThinking](https://core.telegram.org/bots/api#richblockthinking) documentation.
     */
    @TelegramCodegen.Type
    data class Thinking internal constructor(
        /** Text of the block. */
        val text: RichTextValue
    ) : Block {
        override val type = Type.THINKING
        companion object
    }

    /** Type of a block in a rich formatted message. */
    enum class Type {
        /** Case for a text paragraph. */
        @JsonProperty(PARAGRAPH_STR) PARAGRAPH,
        /** Case for a section heading. */
        @JsonProperty(HEADING_STR) HEADING,
        /** Case for a preformatted text block. */
        @JsonProperty(PRE_STR) PRE,
        /** Case for a footer. */
        @JsonProperty(FOOTER_STR) FOOTER,
        /** Case for a divider. */
        @JsonProperty(DIVIDER_STR) DIVIDER,
        /** Case for a mathematical expression. */
        @JsonProperty(MATHEMATICAL_EXPRESSION_STR) MATHEMATICAL_EXPRESSION,
        /** Case for an anchor. */
        @JsonProperty(ANCHOR_STR) ANCHOR,
        /** Case for a list of blocks. */
        @JsonProperty(LIST_STR) LIST,
        /** Case for a block quotation. */
        @JsonProperty(BLOCKQUOTE_STR) BLOCKQUOTE,
        /** Case for a quotation with centered text. */
        @JsonProperty(PULLQUOTE_STR) PULLQUOTE,
        /** Case for a collage. */
        @JsonProperty(COLLAGE_STR) COLLAGE,
        /** Case for a slideshow. */
        @JsonProperty(SLIDESHOW_STR) SLIDESHOW,
        /** Case for a table. */
        @JsonProperty(TABLE_STR) TABLE,
        /** Case for an expandable details disclosure block. */
        @JsonProperty(DETAILS_STR) DETAILS,
        /** Case for a block with a map. */
        @JsonProperty(MAP_STR) MAP,
        /** Case for a block with an animation. */
        @JsonProperty(ANIMATION_STR) ANIMATION,
        /** Case for a block with a music file. */
        @JsonProperty(AUDIO_STR) AUDIO,
        /** Case for a block with a photo. */
        @JsonProperty(PHOTO_STR) PHOTO,
        /** Case for a block with a video. */
        @JsonProperty(VIDEO_STR) VIDEO,
        /** Case for a block with a voice note. */
        @JsonProperty(VOICE_NOTE_STR) VOICE_NOTE,
        /** Case for a block with a “Thinking…” placeholder. */
        @JsonProperty(THINKING_STR) THINKING;

        companion object {
            const val PARAGRAPH_STR = "paragraph"
            const val HEADING_STR = "heading"
            const val PRE_STR = "pre"
            const val FOOTER_STR = "footer"
            const val DIVIDER_STR = "divider"
            const val MATHEMATICAL_EXPRESSION_STR = "mathematical_expression"
            const val ANCHOR_STR = "anchor"
            const val LIST_STR = "list"
            const val BLOCKQUOTE_STR = "blockquote"
            const val PULLQUOTE_STR = "pullquote"
            const val COLLAGE_STR = "collage"
            const val SLIDESHOW_STR = "slideshow"
            const val TABLE_STR = "table"
            const val DETAILS_STR = "details"
            const val MAP_STR = "map"
            const val ANIMATION_STR = "animation"
            const val AUDIO_STR = "audio"
            const val PHOTO_STR = "photo"
            const val VIDEO_STR = "video"
            const val VOICE_NOTE_STR = "voice_note"
            const val THINKING_STR = "thinking"
        }
    }
}
