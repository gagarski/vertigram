package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration

/**
 * Telegram [InputMedia](https://core.telegram.org/bots/api#inputmedia) type.
 *
 * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
 * names given they are nested into [InputMedia] class. The rule here is the following:
 * `InputMediaXxx` Telegram type becomes `InputMedia.Xxx`. The exception is [InputMedia.Sticker]
 * which is not a subtype of [InputMedia].
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputMedia.Animation::class, name = InputMedia.Type.ANIMATION_STR),
    JsonSubTypes.Type(value = InputMedia.Audio::class, name = InputMedia.Type.AUDIO_STR),
    JsonSubTypes.Type(value = InputMedia.Document::class, name = InputMedia.Type.DOCUMENT_STR),
    JsonSubTypes.Type(value = InputMedia.Photo::class, name = InputMedia.Type.PHOTO_STR),
    JsonSubTypes.Type(value = InputMedia.Video::class, name = InputMedia.Type.VIDEO_STR),

)
sealed interface InputMedia {
    val type: Type
    val media: Attachment
    val thumbnail: Attachment?

    fun instantiate(media: Attachment, thumbnail: Attachment?): InputMedia

    /**
     * Telegram [InputMediaAnimation](https://core.telegram.org/bots/api#inputmediaanimation) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class Animation internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Duration? = null,
        val hasSpoiler: Boolean = false
    ) : InputMedia, HasOptionalRichCaption {
        override val type: Type = Type.ANIMATION

        override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)

        companion object
    }

    /**
     * Telegram [InputMediaAudio](https://core.telegram.org/bots/api#inputmediaaudio) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class Audio internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val duration: Duration? = null,
        val performer: String? = null,
        val title: String? = null
    ) : InputMedia, HasOptionalRichCaption {
        override val type: Type = Type.AUDIO
        override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)

        companion object
    }

    /**
     * Telegram [InputMediaDocument](https://core.telegram.org/bots/api#inputmediadocument) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class Document internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val disableContentTypeDetection: Boolean = false
    ) : InputMedia, HasOptionalRichCaption {
        override val type: Type = Type.DOCUMENT
        override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)

        companion object
    }

    /**
     * Telegram [InputMediaPhoto](https://core.telegram.org/bots/api#inputmediaphoto) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class Photo internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val media: Attachment,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val hasSpoiler: Boolean = false
    ) : InputMedia, HasOptionalRichCaption {
        override val type: Type = Type.PHOTO
        override val thumbnail = null
        override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media)

        companion object
    }

    /**
     * Telegram [InputMediaVideo](https://core.telegram.org/bots/api#inputmediavideo) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen(
        generateMethod = false,
        generatePseudoConstructor = true,
    )
    data class Video internal constructor(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Duration? = null,
        val supportsStreaming: Boolean = false,
        val hasSpoiler: Boolean = false
    ) : InputMedia, HasOptionalRichCaption {
        override val type: Type = Type.VIDEO
        override fun instantiate(media: Attachment, thumbnail: Attachment?) = copy(media = media, thumbnail = thumbnail)

        companion object
    }

    /**
     * Telegram [InputSticker](https://core.telegram.org/bots/api#inputsticker) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Sticker(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val sticker: Attachment,
        val format: ski.gagar.vertigram.telegram.types.Sticker.Format,
        val emojiList: List<String>,
        val maskPosition: ski.gagar.vertigram.telegram.types.Sticker.MaskPosition? = null,
        val keywords: List<String>? = null
    ) {
        fun instantiate(sticker: Attachment) = copy(sticker = sticker)

        companion object
    }


    /**
     * Value for [type]
     */
    enum class Type {
        @JsonProperty(PHOTO_STR)
        PHOTO,
        @JsonProperty(VIDEO_STR)
        VIDEO,
        @JsonProperty(ANIMATION_STR)
        ANIMATION,
        @JsonProperty(AUDIO_STR)
        AUDIO,
        @JsonProperty(DOCUMENT_STR)
        DOCUMENT;

        companion object {
            const val PHOTO_STR = "photo"
            const val VIDEO_STR = "video"
            const val ANIMATION_STR = "animation"
            const val AUDIO_STR = "audio"
            const val DOCUMENT_STR = "document"
        }
    }
}

