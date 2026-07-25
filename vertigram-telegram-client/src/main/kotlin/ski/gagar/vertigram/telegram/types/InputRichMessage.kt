package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Describes a rich message to be sent.
 *
 * See Telegram's [InputRichMessage](https://core.telegram.org/bots/api#inputrichmessage) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputRichMessage.Html::class),
    JsonSubTypes.Type(value = InputRichMessage.Markdown::class),
    JsonSubTypes.Type(value = InputRichMessage.Blocks::class)
)
sealed interface InputRichMessage {
    val isRtl: Boolean
    val skipEntityDetection: Boolean

    /** Case when the rich message content is described using HTML formatting. */
    @TelegramCodegen.Type
    data class Html internal constructor(
        /**
         * Content of the rich message to send described using HTML formatting.
         *
         * See [rich message formatting options](https://core.telegram.org/bots/api#inputrichmessage-formatting-options)
         * for more details. Use [media] to specify the media used in the message.
         */
        val html: String,
        /** Whether the rich message must be shown right-to-left. */
        @get:JvmName("getIsRtl")
        override val isRtl: Boolean = false,
        /**
         * Whether to skip automatic detection of entities such as URLs, email addresses, username mentions, hashtags,
         * cashtags, bot commands, or phone numbers in the text.
         */
        override val skipEntityDetection: Boolean = false,
        /**
         * Media specified in [html] using `tg://photo?id=`, `tg://video?id=`, and `tg://audio?id=` links.
         */
        val media: List<InputRichMessageMedia>? = null
    ) : InputRichMessage {
        companion object
    }

    /** Case when the rich message content is described using Markdown formatting. */
    @TelegramCodegen.Type
    data class Markdown internal constructor(
        /**
         * Content of the rich message to send described using Markdown formatting.
         *
         * See [rich message formatting options](https://core.telegram.org/bots/api#inputrichmessage-formatting-options)
         * for more details. Use [media] to specify the media used in the message.
         */
        val markdown: String,
        /** Whether the rich message must be shown right-to-left. */
        @get:JvmName("getIsRtl")
        override val isRtl: Boolean = false,
        /**
         * Whether to skip automatic detection of entities such as URLs, email addresses, username mentions, hashtags,
         * cashtags, bot commands, or phone numbers in the text.
         */
        override val skipEntityDetection: Boolean = false,
        /**
         * Media specified in [markdown] using `tg://photo?id=`, `tg://video?id=`, and `tg://audio?id=` links.
         */
        val media: List<InputRichMessageMedia>? = null
    ) : InputRichMessage {
        companion object
    }

    /** Case when the rich message content is described as a list of blocks. */
    @TelegramCodegen.Type
    data class Blocks internal constructor(
        /** Content of the rich message to send described as a list of blocks. */
        val blocks: List<InputRichBlock>,
        /** Whether the rich message must be shown right-to-left. */
        @get:JvmName("getIsRtl")
        override val isRtl: Boolean = false,
        /**
         * Whether to skip automatic detection of entities such as URLs, email addresses, username mentions, hashtags,
         * cashtags, bot commands, or phone numbers in the text.
         */
        override val skipEntityDetection: Boolean = false
    ) : InputRichMessage {
        companion object
    }
}

/**
 * Describes a media element embedded in an outgoing rich message.
 *
 * See Telegram's [InputRichMessageMedia](https://core.telegram.org/bots/api#inputrichmessagemedia) documentation.
 */
@TelegramCodegen.Type
data class InputRichMessageMedia internal constructor(
    /**
     * Unique identifier of the media, used in `tg://photo?id=`, `tg://video?id=`, or `tg://audio?id=` links. Must be
     * 1-64 characters long and contain only English letters, digits, underscores, and hyphens.
     */
    val id: String,
    /** Media to send. All fields other than the media itself and its properties will be ignored. */
    val media: InputMedia.RichMessage
) {
    companion object
}
