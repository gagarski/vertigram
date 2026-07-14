package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richmessage.Caption
import ski.gagar.vertigram.telegram.types.richmessage.RichTextValue
import ski.gagar.vertigram.telegram.types.richmessage.TableCell

/**
 * Telegram [InputRichBlockListItem](https://core.telegram.org/bots/api#inputrichblocklistitem) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class InputRichBlockListItem internal constructor(
    val blocks: List<InputRichBlock>,
    val hasCheckbox: Boolean = false,
    @get:JvmName("getIsChecked")
    val isChecked: Boolean = false,
    val value: Int? = null,
    val type: String? = null
) {
    companion object
}

/**
 * Telegram [InputRichBlock](https://core.telegram.org/bots/api#inputrichblock) type.
 *
 * Subtypes represent the supported outgoing rich-message blocks.
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputRichBlock.Paragraph::class, name = InputRichBlock.Type.PARAGRAPH_STR),
    JsonSubTypes.Type(value = InputRichBlock.SectionHeading::class, name = InputRichBlock.Type.HEADING_STR),
    JsonSubTypes.Type(value = InputRichBlock.Preformatted::class, name = InputRichBlock.Type.PRE_STR),
    JsonSubTypes.Type(value = InputRichBlock.Footer::class, name = InputRichBlock.Type.FOOTER_STR),
    JsonSubTypes.Type(value = InputRichBlock.Divider::class, name = InputRichBlock.Type.DIVIDER_STR),
    JsonSubTypes.Type(value = InputRichBlock.MathematicalExpression::class, name = InputRichBlock.Type.MATH_STR),
    JsonSubTypes.Type(value = InputRichBlock.Anchor::class, name = InputRichBlock.Type.ANCHOR_STR),
    JsonSubTypes.Type(value = InputRichBlock.List::class, name = InputRichBlock.Type.LIST_STR),
    JsonSubTypes.Type(value = InputRichBlock.BlockQuotation::class, name = InputRichBlock.Type.BLOCKQUOTE_STR),
    JsonSubTypes.Type(value = InputRichBlock.PullQuotation::class, name = InputRichBlock.Type.PULLQUOTE_STR),
    JsonSubTypes.Type(value = InputRichBlock.Collage::class, name = InputRichBlock.Type.COLLAGE_STR),
    JsonSubTypes.Type(value = InputRichBlock.Slideshow::class, name = InputRichBlock.Type.SLIDESHOW_STR),
    JsonSubTypes.Type(value = InputRichBlock.Table::class, name = InputRichBlock.Type.TABLE_STR),
    JsonSubTypes.Type(value = InputRichBlock.Details::class, name = InputRichBlock.Type.DETAILS_STR),
    JsonSubTypes.Type(value = InputRichBlock.Map::class, name = InputRichBlock.Type.MAP_STR),
    JsonSubTypes.Type(value = InputRichBlock.Animation::class, name = InputRichBlock.Type.ANIMATION_STR),
    JsonSubTypes.Type(value = InputRichBlock.Audio::class, name = InputRichBlock.Type.AUDIO_STR),
    JsonSubTypes.Type(value = InputRichBlock.Photo::class, name = InputRichBlock.Type.PHOTO_STR),
    JsonSubTypes.Type(value = InputRichBlock.Video::class, name = InputRichBlock.Type.VIDEO_STR),
    JsonSubTypes.Type(value = InputRichBlock.VoiceNote::class, name = InputRichBlock.Type.VOICE_NOTE_STR),
    JsonSubTypes.Type(value = InputRichBlock.Thinking::class, name = InputRichBlock.Type.THINKING_STR)
)
sealed interface InputRichBlock {
    val type: Type

    /** Telegram [InputRichBlockParagraph](https://core.telegram.org/bots/api#inputrichblockparagraph) type. */
    @TelegramCodegen.Type
    data class Paragraph internal constructor(val text: RichTextValue) : InputRichBlock {
        override val type = Type.PARAGRAPH
        companion object
    }

    /** Telegram [InputRichBlockSectionHeading](https://core.telegram.org/bots/api#inputrichblocksectionheading) type. */
    @TelegramCodegen.Type
    data class SectionHeading internal constructor(val text: RichTextValue, val size: Int) : InputRichBlock {
        override val type = Type.HEADING
        companion object
    }

    /** Telegram [InputRichBlockPreformatted](https://core.telegram.org/bots/api#inputrichblockpreformatted) type. */
    @TelegramCodegen.Type
    data class Preformatted internal constructor(
        val text: RichTextValue,
        val language: String? = null
    ) : InputRichBlock {
        override val type = Type.PRE
        companion object
    }

    /** Telegram [InputRichBlockFooter](https://core.telegram.org/bots/api#inputrichblockfooter) type. */
    @TelegramCodegen.Type
    data class Footer internal constructor(val text: RichTextValue) : InputRichBlock {
        override val type = Type.FOOTER
        companion object
    }

    /** Telegram [InputRichBlockDivider](https://core.telegram.org/bots/api#inputrichblockdivider) type. */
    data object Divider : InputRichBlock {
        override val type = Type.DIVIDER
    }

    /** Telegram [InputRichBlockMathematicalExpression](https://core.telegram.org/bots/api#inputrichblockmathematicalexpression) type. */
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(val expression: String) : InputRichBlock {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }

    /** Telegram [InputRichBlockAnchor](https://core.telegram.org/bots/api#inputrichblockanchor) type. */
    @TelegramCodegen.Type
    data class Anchor internal constructor(val name: String) : InputRichBlock {
        override val type = Type.ANCHOR
        companion object
    }

    /** Telegram [InputRichBlockList](https://core.telegram.org/bots/api#inputrichblocklist) type. */
    @TelegramCodegen.Type
    data class List internal constructor(
        val items: kotlin.collections.List<InputRichBlockListItem>
    ) : InputRichBlock {
        override val type = Type.LIST
        companion object
    }

    /** Telegram [InputRichBlockBlockQuotation](https://core.telegram.org/bots/api#inputrichblockblockquotation) type. */
    @TelegramCodegen.Type
    data class BlockQuotation internal constructor(
        val blocks: kotlin.collections.List<InputRichBlock>,
        val credit: RichTextValue? = null
    ) : InputRichBlock {
        override val type = Type.BLOCKQUOTE
        companion object
    }

    /** Telegram [InputRichBlockPullQuotation](https://core.telegram.org/bots/api#inputrichblockpullquotation) type. */
    @TelegramCodegen.Type
    data class PullQuotation internal constructor(
        val text: RichTextValue,
        val credit: RichTextValue? = null
    ) : InputRichBlock {
        override val type = Type.PULLQUOTE
        companion object
    }

    /** Telegram [InputRichBlockCollage](https://core.telegram.org/bots/api#inputrichblockcollage) type. */
    @TelegramCodegen.Type
    data class Collage internal constructor(
        val blocks: kotlin.collections.List<InputRichBlock>,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.COLLAGE
        companion object
    }

    /** Telegram [InputRichBlockSlideshow](https://core.telegram.org/bots/api#inputrichblockslideshow) type. */
    @TelegramCodegen.Type
    data class Slideshow internal constructor(
        val blocks: kotlin.collections.List<InputRichBlock>,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.SLIDESHOW
        companion object
    }

    /** Telegram [InputRichBlockTable](https://core.telegram.org/bots/api#inputrichblocktable) type. */
    @TelegramCodegen.Type
    data class Table internal constructor(
        val cells: kotlin.collections.List<kotlin.collections.List<TableCell>>,
        @get:JvmName("getIsBordered")
        val isBordered: Boolean = false,
        @get:JvmName("getIsStriped")
        val isStriped: Boolean = false,
        val caption: RichTextValue? = null
    ) : InputRichBlock {
        override val type = Type.TABLE
        companion object
    }

    /** Telegram [InputRichBlockDetails](https://core.telegram.org/bots/api#inputrichblockdetails) type. */
    @TelegramCodegen.Type
    data class Details internal constructor(
        val summary: RichTextValue,
        val blocks: kotlin.collections.List<InputRichBlock>,
        @get:JvmName("getIsOpen")
        val isOpen: Boolean = false
    ) : InputRichBlock {
        override val type = Type.DETAILS
        companion object
    }

    /** Telegram [InputRichBlockMap](https://core.telegram.org/bots/api#inputrichblockmap) type. */
    @TelegramCodegen.Type
    data class Map internal constructor(
        val location: Location,
        val zoom: Int,
        val width: Int,
        val height: Int,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.MAP
        companion object
    }

    /** Telegram [InputRichBlockAnimation](https://core.telegram.org/bots/api#inputrichblockanimation) type. */
    @TelegramCodegen.Type
    data class Animation internal constructor(
        val animation: InputMedia.Animation,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.ANIMATION
        companion object
    }

    /** Telegram [InputRichBlockAudio](https://core.telegram.org/bots/api#inputrichblockaudio) type. */
    @TelegramCodegen.Type
    data class Audio internal constructor(
        val audio: InputMedia.Audio,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.AUDIO
        companion object
    }

    /** Telegram [InputRichBlockPhoto](https://core.telegram.org/bots/api#inputrichblockphoto) type. */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        val photo: InputMedia.Photo,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.PHOTO
        companion object
    }

    /** Telegram [InputRichBlockVideo](https://core.telegram.org/bots/api#inputrichblockvideo) type. */
    @TelegramCodegen.Type
    data class Video internal constructor(
        val video: InputMedia.Video,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.VIDEO
        companion object
    }

    /** Telegram [InputRichBlockVoiceNote](https://core.telegram.org/bots/api#inputrichblockvoicenote) type. */
    @TelegramCodegen.Type
    data class VoiceNote internal constructor(
        val voiceNote: InputMedia.VoiceNote,
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.VOICE_NOTE
        companion object
    }

    /** Telegram [InputRichBlockThinking](https://core.telegram.org/bots/api#inputrichblockthinking) type. */
    @TelegramCodegen.Type
    data class Thinking internal constructor(val text: RichTextValue) : InputRichBlock {
        override val type = Type.THINKING
        companion object
    }

    enum class Type {
        @JsonProperty(PARAGRAPH_STR) PARAGRAPH,
        @JsonProperty(HEADING_STR) HEADING,
        @JsonProperty(PRE_STR) PRE,
        @JsonProperty(FOOTER_STR) FOOTER,
        @JsonProperty(DIVIDER_STR) DIVIDER,
        @JsonProperty(MATH_STR) MATHEMATICAL_EXPRESSION,
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
            const val HEADING_STR = "heading"
            const val PRE_STR = "pre"
            const val FOOTER_STR = "footer"
            const val DIVIDER_STR = "divider"
            const val MATH_STR = "mathematical_expression"
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
