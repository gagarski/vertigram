package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.richmessage.Caption
import ski.gagar.vertigram.telegram.types.richmessage.RichTextValue
import ski.gagar.vertigram.telegram.types.richmessage.TableCell

/**
 * An item of a list to be sent.
 *
 * See Telegram's [InputRichBlockListItem](https://core.telegram.org/bots/api#inputrichblocklistitem) documentation.
 */
@TelegramCodegen.Type
data class InputRichBlockListItem internal constructor(
    /** Content of the item. */
    val blocks: List<InputRichBlock>,
    /** Whether the item has a checkbox. */
    val hasCheckbox: Boolean = false,
    /** Whether the item has a checked checkbox. */
    @get:JvmName("getIsChecked")
    val isChecked: Boolean = false,
    /** For ordered lists, the numeric value of the item label. */
    val value: Int? = null,
    /**
     * For ordered lists, the type of the item label: `a` for lowercase letters, `A` for uppercase letters, `i` for
     * lowercase Roman numerals, `I` for uppercase Roman numerals, or `1` for decimal numbers.
     */
    val type: String? = null
) {
    companion object
}

/**
 * Represents a block in a rich formatted message to be sent.
 *
 * See Telegram's [InputRichBlock](https://core.telegram.org/bots/api#inputrichblock) documentation.
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

    /**
     * Case when the block is a text paragraph, corresponding to the HTML tag `<p>`.
     *
     * See Telegram's [InputRichBlockParagraph](https://core.telegram.org/bots/api#inputrichblockparagraph)
     * documentation.
     */
    @TelegramCodegen.Type
    data class Paragraph internal constructor(
        /** Text of the block. */
        val text: RichTextValue
    ) : InputRichBlock {
        override val type = Type.PARAGRAPH
        companion object
    }

    /**
     * Case when the block is a section heading, corresponding to the HTML tags `<h1>` through `<h6>`.
     *
     * See Telegram's [InputRichBlockSectionHeading](https://core.telegram.org/bots/api#inputrichblocksectionheading)
     * documentation.
     */
    @TelegramCodegen.Type
    data class SectionHeading internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Relative size of the text font; 1-6, 1 is the largest and 6 is the smallest. */
        val size: Int
    ) : InputRichBlock {
        override val type = Type.HEADING
        companion object
    }

    /**
     * Case when the block contains preformatted text, corresponding to the nested HTML tags `<pre>` and `<code>`.
     *
     * See Telegram's [InputRichBlockPreformatted](https://core.telegram.org/bots/api#inputrichblockpreformatted)
     * documentation.
     */
    @TelegramCodegen.Type
    data class Preformatted internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Programming language of the text. */
        val language: String? = null
    ) : InputRichBlock {
        override val type = Type.PRE
        companion object
    }

    /**
     * Case when the block is a footer, corresponding to the HTML tag `<footer>`.
     *
     * See Telegram's [InputRichBlockFooter](https://core.telegram.org/bots/api#inputrichblockfooter) documentation.
     */
    @TelegramCodegen.Type
    data class Footer internal constructor(
        /** Text of the block. */
        val text: RichTextValue
    ) : InputRichBlock {
        override val type = Type.FOOTER
        companion object
    }

    /**
     * Case when the block is a divider, corresponding to the HTML tag `<hr/>`.
     *
     * See Telegram's [InputRichBlockDivider](https://core.telegram.org/bots/api#inputrichblockdivider) documentation.
     */
    data object Divider : InputRichBlock {
        override val type = Type.DIVIDER
    }

    /**
     * Case when the block contains a mathematical expression in LaTeX format, corresponding to the custom HTML tag
     * `<tg-math-block>`.
     *
     * See Telegram's
     * [InputRichBlockMathematicalExpression](https://core.telegram.org/bots/api#inputrichblockmathematicalexpression)
     * documentation.
     */
    @TelegramCodegen.Type
    data class MathematicalExpression internal constructor(
        /** Mathematical expression in LaTeX format. */
        val expression: String
    ) : InputRichBlock {
        override val type = Type.MATHEMATICAL_EXPRESSION
        companion object
    }

    /**
     * Case when the block contains an anchor, corresponding to the HTML tag `<a>` with the attribute `name`.
     *
     * See Telegram's [InputRichBlockAnchor](https://core.telegram.org/bots/api#inputrichblockanchor) documentation.
     */
    @TelegramCodegen.Type
    data class Anchor internal constructor(
        /** Name of the anchor. */
        val name: String
    ) : InputRichBlock {
        override val type = Type.ANCHOR
        companion object
    }

    /**
     * Case when the block is a list, corresponding to the HTML tag `<ul>` or `<ol>` with nested `<li>` tags.
     *
     * See Telegram's [InputRichBlockList](https://core.telegram.org/bots/api#inputrichblocklist) documentation.
     */
    @TelegramCodegen.Type
    data class List internal constructor(
        /** Items of the list. */
        val items: kotlin.collections.List<InputRichBlockListItem>
    ) : InputRichBlock {
        override val type = Type.LIST
        companion object
    }

    /**
     * Case when the block is a block quotation, corresponding to the HTML tag `<blockquote>`.
     *
     * See Telegram's
     * [InputRichBlockBlockQuotation](https://core.telegram.org/bots/api#inputrichblockblockquotation) documentation.
     */
    @TelegramCodegen.Type
    data class BlockQuotation internal constructor(
        /** Content of the block. */
        val blocks: kotlin.collections.List<InputRichBlock>,
        /** Credit of the block. */
        val credit: RichTextValue? = null
    ) : InputRichBlock {
        override val type = Type.BLOCKQUOTE
        companion object
    }

    /**
     * Case when the block is a quotation with centered text, loosely corresponding to the HTML tag `<aside>`.
     *
     * See Telegram's
     * [InputRichBlockPullQuotation](https://core.telegram.org/bots/api#inputrichblockpullquotation) documentation.
     */
    @TelegramCodegen.Type
    data class PullQuotation internal constructor(
        /** Text of the block. */
        val text: RichTextValue,
        /** Credit of the block. */
        val credit: RichTextValue? = null
    ) : InputRichBlock {
        override val type = Type.PULLQUOTE
        companion object
    }

    /**
     * Case when the block is a collage, corresponding to the custom HTML tag `<tg-collage>`.
     *
     * See Telegram's [InputRichBlockCollage](https://core.telegram.org/bots/api#inputrichblockcollage) documentation.
     */
    @TelegramCodegen.Type
    data class Collage internal constructor(
        /** Elements of the collage. */
        val blocks: kotlin.collections.List<InputRichBlock>,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.COLLAGE
        companion object
    }

    /**
     * Case when the block is a slideshow, corresponding to the custom HTML tag `<tg-slideshow>`.
     *
     * See Telegram's
     * [InputRichBlockSlideshow](https://core.telegram.org/bots/api#inputrichblockslideshow) documentation.
     */
    @TelegramCodegen.Type
    data class Slideshow internal constructor(
        /** Elements of the slideshow. */
        val blocks: kotlin.collections.List<InputRichBlock>,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.SLIDESHOW
        companion object
    }

    /**
     * Case when the block is a table, corresponding to the HTML tag `<table>`.
     *
     * See Telegram's [InputRichBlockTable](https://core.telegram.org/bots/api#inputrichblocktable) documentation.
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
    ) : InputRichBlock {
        override val type = Type.TABLE
        companion object
    }

    /**
     * Case when the block is expandable for details disclosure, corresponding to the HTML tag `<details>`.
     *
     * See Telegram's [InputRichBlockDetails](https://core.telegram.org/bots/api#inputrichblockdetails) documentation.
     */
    @TelegramCodegen.Type
    data class Details internal constructor(
        /** Always shown summary of the block. */
        val summary: RichTextValue,
        /** Content of the block. */
        val blocks: kotlin.collections.List<InputRichBlock>,
        /** Whether the content of the block is visible by default. */
        @get:JvmName("getIsOpen")
        val isOpen: Boolean = false
    ) : InputRichBlock {
        override val type = Type.DETAILS
        companion object
    }

    /**
     * Case when the block contains a map, corresponding to the custom HTML tag `<tg-map>`.
     *
     * The map's width and height must not exceed 10000 in total. The width-to-height ratio must be at most 20.
     *
     * See Telegram's [InputRichBlockMap](https://core.telegram.org/bots/api#inputrichblockmap) documentation.
     */
    @TelegramCodegen.Type
    data class Map internal constructor(
        /** Location of the center of the map. */
        val location: Location,
        /** Map zoom level; 0-24. */
        val zoom: Int,
        /** Map width; 0-10000. */
        val width: Int,
        /** Map height; 0-10000. */
        val height: Int,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.MAP
        companion object
    }

    /**
     * Case when the block contains an animation, corresponding to the HTML tag `<video>`.
     *
     * See Telegram's
     * [InputRichBlockAnimation](https://core.telegram.org/bots/api#inputrichblockanimation) documentation.
     */
    @TelegramCodegen.Type
    data class Animation internal constructor(
        /** Animation to send. Its caption is ignored. */
        val animation: InputMedia.Animation,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.ANIMATION
        companion object
    }

    /**
     * Case when the block contains a music file, corresponding to the HTML tag `<audio>`.
     *
     * See Telegram's [InputRichBlockAudio](https://core.telegram.org/bots/api#inputrichblockaudio) documentation.
     */
    @TelegramCodegen.Type
    data class Audio internal constructor(
        /** Audio to send. Its caption is ignored. */
        val audio: InputMedia.Audio,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.AUDIO
        companion object
    }

    /**
     * Case when the block contains a photo, corresponding to the HTML tag `<img>`.
     *
     * See Telegram's [InputRichBlockPhoto](https://core.telegram.org/bots/api#inputrichblockphoto) documentation.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        /** Photo to send. Its caption is ignored. */
        val photo: InputMedia.Photo,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.PHOTO
        companion object
    }

    /**
     * Case when the block contains a video, corresponding to the HTML tag `<video>`.
     *
     * See Telegram's [InputRichBlockVideo](https://core.telegram.org/bots/api#inputrichblockvideo) documentation.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
        /** Video to send. Its caption is ignored. */
        val video: InputMedia.Video,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.VIDEO
        companion object
    }

    /**
     * Case when the block contains a voice note, corresponding to the HTML tag `<audio>`.
     *
     * See Telegram's
     * [InputRichBlockVoiceNote](https://core.telegram.org/bots/api#inputrichblockvoicenote) documentation.
     */
    @TelegramCodegen.Type
    data class VoiceNote internal constructor(
        /** Voice note to send. Its caption is ignored. */
        val voiceNote: InputMedia.VoiceNote,
        /** Caption of the block. */
        val caption: Caption? = null
    ) : InputRichBlock {
        override val type = Type.VOICE_NOTE
        companion object
    }

    /**
     * Case when the block contains a “Thinking…” placeholder, corresponding to the custom HTML tag `<tg-thinking>`.
     *
     * This block may be used only in
     * [sendRichMessageDraft][ski.gagar.vertigram.telegram.methods.sendRichMessageDraft] and can't be received in
     * messages. See [recommended custom emoji](https://t.me/addemoji/AIActions) for examples.
     *
     * See Telegram's [InputRichBlockThinking](https://core.telegram.org/bots/api#inputrichblockthinking)
     * documentation.
     */
    @TelegramCodegen.Type
    data class Thinking internal constructor(
        /**
         * Text of the block. See [recommended custom emoji](https://t.me/addemoji/AIActions) for examples.
         */
        val text: RichTextValue
    ) : InputRichBlock {
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
