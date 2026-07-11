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
 * Telegram [RichMessage](https://core.telegram.org/bots/api#richmessage) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class RichMessage internal constructor(
    val blocks: List<Block>,
    @get:JvmName("getIsRtl")
    val isRtl: Boolean = false
) {
    companion object
}

/**
 * Telegram [RichText](https://core.telegram.org/bots/api#richtext) object cases.
 *
 * The Bot API can also return plain strings and arrays as rich text values. See [RichTextValue].
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
    val type: Type

    @TelegramCodegen.Type
    data class Bold internal constructor(val text: RichTextValue) : RichText { override val type = Type.BOLD; companion object }
    @TelegramCodegen.Type
    data class Italic internal constructor(val text: RichTextValue) : RichText { override val type = Type.ITALIC; companion object }
    @TelegramCodegen.Type
    data class Underline internal constructor(val text: RichTextValue) : RichText { override val type = Type.UNDERLINE; companion object }
    @TelegramCodegen.Type
    data class Strikethrough internal constructor(val text: RichTextValue) : RichText { override val type = Type.STRIKETHROUGH; companion object }
    @TelegramCodegen.Type
    data class Spoiler internal constructor(val text: RichTextValue) : RichText { override val type = Type.SPOILER; companion object }
    @TelegramCodegen.Type
    data class DateTime internal constructor(val text: RichTextValue, val unixTime: Instant, val dateTimeFormat: String) : RichText {
        override val type = Type.DATE_TIME
        companion object
    }
    @TelegramCodegen.Type
    data class TextMention internal constructor(val text: RichTextValue, val user: User) : RichText {
        override val type = Type.TEXT_MENTION
        companion object
    }
    @TelegramCodegen.Type
    data class Subscript internal constructor(val text: RichTextValue) : RichText { override val type = Type.SUBSCRIPT; companion object }
    @TelegramCodegen.Type
    data class Superscript internal constructor(val text: RichTextValue) : RichText { override val type = Type.SUPERSCRIPT; companion object }
    @TelegramCodegen.Type
    data class Marked internal constructor(val text: RichTextValue) : RichText { override val type = Type.MARKED; companion object }
    @TelegramCodegen.Type
    data class Code internal constructor(val text: RichTextValue) : RichText { override val type = Type.CODE; companion object }
    @TelegramCodegen.Type
    data class CustomEmoji internal constructor(val customEmojiId: String, val alternativeText: String) : RichText {
        override val type = Type.CUSTOM_EMOJI
        companion object
    }
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(val expression: String) : RichText {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }
    @TelegramCodegen.Type
    data class Url internal constructor(val text: RichTextValue, val url: String) : RichText { override val type = Type.URL; companion object }
    @TelegramCodegen.Type
    data class EmailAddress internal constructor(val text: RichTextValue, val emailAddress: String) : RichText {
        override val type = Type.EMAIL_ADDRESS
        companion object
    }
    @TelegramCodegen.Type
    data class PhoneNumber internal constructor(val text: RichTextValue, val phoneNumber: String) : RichText {
        override val type = Type.PHONE_NUMBER
        companion object
    }
    @TelegramCodegen.Type
    data class BankCardNumber internal constructor(val text: RichTextValue, val bankCardNumber: String) : RichText {
        override val type = Type.BANK_CARD_NUMBER
        companion object
    }
    @TelegramCodegen.Type
    data class Mention internal constructor(val text: RichTextValue, val username: String) : RichText {
        override val type = Type.MENTION
        companion object
    }
    @TelegramCodegen.Type
    data class Hashtag internal constructor(val text: RichTextValue, val hashtag: String) : RichText {
        override val type = Type.HASHTAG
        companion object
    }
    @TelegramCodegen.Type
    data class Cashtag internal constructor(val text: RichTextValue, val cashtag: String) : RichText {
        override val type = Type.CASHTAG
        companion object
    }
    @TelegramCodegen.Type
    data class BotCommandText internal constructor(val text: RichTextValue, val botCommand: String) : RichText {
        override val type = Type.BOT_COMMAND
        companion object
    }
    @TelegramCodegen.Type
    data class Anchor internal constructor(val name: String) : RichText { override val type = Type.ANCHOR; companion object }
    @TelegramCodegen.Type
    data class AnchorLink internal constructor(val text: RichTextValue, val anchorName: String) : RichText {
        override val type = Type.ANCHOR_LINK
        companion object
    }
    @TelegramCodegen.Type
    data class Reference internal constructor(val name: String, val text: RichTextValue) : RichText {
        override val type = Type.REFERENCE
        companion object
    }
    @TelegramCodegen.Type
    data class ReferenceLink internal constructor(val text: RichTextValue, val referenceName: String) : RichText {
        override val type = Type.REFERENCE_LINK
        companion object
    }

    enum class Type {
        @JsonProperty(BOLD_STR) BOLD,
        @JsonProperty(ITALIC_STR) ITALIC,
        @JsonProperty(UNDERLINE_STR) UNDERLINE,
        @JsonProperty(STRIKETHROUGH_STR) STRIKETHROUGH,
        @JsonProperty(SPOILER_STR) SPOILER,
        @JsonProperty(DATE_TIME_STR) DATE_TIME,
        @JsonProperty(TEXT_MENTION_STR) TEXT_MENTION,
        @JsonProperty(SUBSCRIPT_STR) SUBSCRIPT,
        @JsonProperty(SUPERSCRIPT_STR) SUPERSCRIPT,
        @JsonProperty(MARKED_STR) MARKED,
        @JsonProperty(CODE_STR) CODE,
        @JsonProperty(CUSTOM_EMOJI_STR) CUSTOM_EMOJI,
        @JsonProperty(MATHEMATICAL_EXPRESSION_STR) MATHEMATICAL_EXPRESSION,
        @JsonProperty(URL_STR) URL,
        @JsonProperty(EMAIL_ADDRESS_STR) EMAIL_ADDRESS,
        @JsonProperty(PHONE_NUMBER_STR) PHONE_NUMBER,
        @JsonProperty(BANK_CARD_NUMBER_STR) BANK_CARD_NUMBER,
        @JsonProperty(MENTION_STR) MENTION,
        @JsonProperty(HASHTAG_STR) HASHTAG,
        @JsonProperty(CASHTAG_STR) CASHTAG,
        @JsonProperty(BOT_COMMAND_STR) BOT_COMMAND,
        @JsonProperty(ANCHOR_STR) ANCHOR,
        @JsonProperty(ANCHOR_LINK_STR) ANCHOR_LINK,
        @JsonProperty(REFERENCE_STR) REFERENCE,
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
 * Telegram rich text value: a plain string, an array of rich text values, or an object case from [RichText].
 */
@JsonSerialize(using = RichTextValue.Serializer::class)
@JsonDeserialize(using = RichTextValue.Deserializer::class)
sealed interface RichTextValue {
    data class Plain internal constructor(val value: String) : RichTextValue
    data class Parts internal constructor(val values: List<RichTextValue>) : RichTextValue
    data class Formatted internal constructor(val value: RichText) : RichTextValue

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
        fun plain(value: String): RichTextValue = Plain(value)
        fun parts(values: List<RichTextValue>): RichTextValue = Parts(values)
        fun formatted(value: RichText): RichTextValue = Formatted(value)
    }
}

@TelegramCodegen.Type
data class Caption internal constructor(
    val text: RichTextValue,
    val credit: RichTextValue? = null
) {
    companion object
}

@TelegramCodegen.Type
data class TableCell internal constructor(
    val text: RichTextValue,
    @get:JvmName("getIsHeader")
    val isHeader: Boolean = false,
    val colspan: Int? = null,
    val rowspan: Int? = null,
    val align: Align? = null,
    val verticalAlign: VerticalAlign? = null
) {
    enum class Align {
        @JsonProperty("left") LEFT,
        @JsonProperty("center") CENTER,
        @JsonProperty("right") RIGHT
    }
    enum class VerticalAlign {
        @JsonProperty("top") TOP,
        @JsonProperty("middle") MIDDLE,
        @JsonProperty("bottom") BOTTOM
    }
    companion object
}

@TelegramCodegen.Type
data class ListItem internal constructor(
    val blocks: List<Block>,
    val number: String? = null,
    val checked: Boolean? = null
) {
    companion object
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = Block.Paragraph::class, name = Block.Type.PARAGRAPH_STR),
    JsonSubTypes.Type(value = Block.SectionHeading::class, name = Block.Type.SECTION_HEADING_STR),
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
sealed interface Block {
    val type: Type

    @TelegramCodegen.Type
    data class Paragraph internal constructor(val text: RichTextValue) : Block { override val type = Type.PARAGRAPH; companion object }
    @TelegramCodegen.Type
    data class SectionHeading internal constructor(val text: RichTextValue, val level: Int) : Block {
        override val type = Type.SECTION_HEADING
        companion object
    }
    @TelegramCodegen.Type
    data class Preformatted internal constructor(val text: RichTextValue, val language: String? = null) : Block {
        override val type = Type.PRE
        companion object
    }
    @TelegramCodegen.Type
    data class Footer internal constructor(val text: RichTextValue) : Block { override val type = Type.FOOTER; companion object }
    data object Divider : Block { override val type = Type.DIVIDER }
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(val expression: String) : Block {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }
    @TelegramCodegen.Type
    data class Anchor internal constructor(val name: String) : Block { override val type = Type.ANCHOR; companion object }
    @TelegramCodegen.Type
    data class List internal constructor(val items: kotlin.collections.List<ListItem>) : Block {
        override val type = Type.LIST
        companion object
    }
    @TelegramCodegen.Type
    data class BlockQuotation internal constructor(val blocks: kotlin.collections.List<Block>, val credit: RichTextValue? = null) : Block {
        override val type = Type.BLOCKQUOTE
        companion object
    }
    @TelegramCodegen.Type
    data class PullQuotation internal constructor(val text: RichTextValue, val credit: RichTextValue? = null) : Block {
        override val type = Type.PULLQUOTE
        companion object
    }
    @TelegramCodegen.Type
    data class Collage internal constructor(val blocks: kotlin.collections.List<Block>, val caption: Caption? = null) : Block {
        override val type = Type.COLLAGE
        companion object
    }
    @TelegramCodegen.Type
    data class Slideshow internal constructor(val blocks: kotlin.collections.List<Block>, val caption: Caption? = null) : Block {
        override val type = Type.SLIDESHOW
        companion object
    }
    @TelegramCodegen.Type
    data class Table internal constructor(
        val cells: kotlin.collections.List<kotlin.collections.List<TableCell>>,
        @get:JvmName("getIsBordered")
        val isBordered: Boolean = false,
        @get:JvmName("getIsStriped")
        val isStriped: Boolean = false,
        val caption: RichTextValue? = null
    ) : Block {
        override val type = Type.TABLE
        companion object
    }
    @TelegramCodegen.Type
    data class Details internal constructor(
        val summary: RichTextValue,
        val blocks: kotlin.collections.List<Block>,
        @get:JvmName("getIsOpen")
        val isOpen: Boolean = false
    ) : Block {
        override val type = Type.DETAILS
        companion object
    }
    @TelegramCodegen.Type
    data class Map internal constructor(val location: Location, val zoom: Int, val width: Int, val height: Int, val caption: Caption? = null) : Block {
        override val type = Type.MAP
        companion object
    }
    @TelegramCodegen.Type
    data class Animation internal constructor(val animation: ski.gagar.vertigram.telegram.types.Animation, val hasSpoiler: Boolean = false, val caption: Caption? = null) : Block {
        override val type = Type.ANIMATION
        companion object
    }
    @TelegramCodegen.Type
    data class Audio internal constructor(val audio: ski.gagar.vertigram.telegram.types.Audio, val caption: Caption? = null) : Block {
        override val type = Type.AUDIO
        companion object
    }
    @TelegramCodegen.Type
    data class Photo internal constructor(val photo: kotlin.collections.List<PhotoSize>, val hasSpoiler: Boolean = false, val caption: Caption? = null) : Block {
        override val type = Type.PHOTO
        companion object
    }
    @TelegramCodegen.Type
    data class Video internal constructor(val video: ski.gagar.vertigram.telegram.types.Video, val hasSpoiler: Boolean = false, val caption: Caption? = null) : Block {
        override val type = Type.VIDEO
        companion object
    }
    @TelegramCodegen.Type
    data class VoiceNote internal constructor(val voiceNote: Voice, val caption: Caption? = null) : Block {
        override val type = Type.VOICE_NOTE
        companion object
    }
    @TelegramCodegen.Type
    data class Thinking internal constructor(val text: RichTextValue) : Block {
        override val type = Type.THINKING
        companion object
    }

    enum class Type {
        @JsonProperty(PARAGRAPH_STR) PARAGRAPH,
        @JsonProperty(SECTION_HEADING_STR) SECTION_HEADING,
        @JsonProperty(PRE_STR) PRE,
        @JsonProperty(FOOTER_STR) FOOTER,
        @JsonProperty(DIVIDER_STR) DIVIDER,
        @JsonProperty(MATHEMATICAL_EXPRESSION_STR) MATHEMATICAL_EXPRESSION,
        @JsonProperty(ANCHOR_STR) ANCHOR,
        @JsonProperty(LIST_STR) LIST,
        @JsonProperty(BLOCKQUOTE_STR) BLOCKQUOTE,
        @JsonProperty(PULLQUOTE_STR) PULLQUOTE,
        @JsonProperty(COLLAGE_STR) COLLAGE,
        @JsonProperty(SLIDESHOW_STR) SLIDESHOW,
        @JsonProperty(TABLE_STR) TABLE,
        @JsonProperty(DETAILS_STR) DETAILS,
        @JsonProperty(MAP_STR) MAP,
        @JsonProperty(ANIMATION_STR) ANIMATION,
        @JsonProperty(AUDIO_STR) AUDIO,
        @JsonProperty(PHOTO_STR) PHOTO,
        @JsonProperty(VIDEO_STR) VIDEO,
        @JsonProperty(VOICE_NOTE_STR) VOICE_NOTE,
        @JsonProperty(THINKING_STR) THINKING;

        companion object {
            const val PARAGRAPH_STR = "paragraph"
            const val SECTION_HEADING_STR = "section_heading"
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
