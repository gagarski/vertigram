package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalRichCaption
import ski.gagar.vertigram.telegram.types.richtext.RichText
import ski.gagar.vertigram.util.json.annotations.Fractional
import java.time.Duration

interface BaseInputMedia<T> {
    val type: T
    val media: Attachment
    val thumbnail: Attachment?
    val cover: Attachment?
}

/**
 * Telegram [InputMedia](https://core.telegram.org/bots/api#inputmedia) type.
 *
 * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
 * names given they are nested into [InputMedia] class. The rule here is the following:
 * `InputMediaXxx` Telegram type becomes `InputMedia.Xxx`. The exception is [InputMedia.Sticker]
 * which is not a subtype of [InputMedia].
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputMedia.Animation::class, name = InputMedia.Type.ANIMATION_STR),
    JsonSubTypes.Type(value = InputMedia.Audio::class, name = InputMedia.Type.AUDIO_STR),
    JsonSubTypes.Type(value = InputMedia.Document::class, name = InputMedia.Type.DOCUMENT_STR),
    JsonSubTypes.Type(value = InputMedia.LivePhoto::class, name = InputMedia.Type.LIVE_PHOTO_STR),
    JsonSubTypes.Type(value = InputMedia.Photo::class, name = InputMedia.Type.PHOTO_STR),
    JsonSubTypes.Type(value = InputMedia.Video::class, name = InputMedia.Type.VIDEO_STR),
    JsonSubTypes.Type(value = InputMedia.VoiceNote::class, name = InputMedia.Type.VOICE_NOTE_STR),
)
sealed interface InputMedia : BaseInputMedia<InputMedia.Type> {
    override val type: Type
    override val media: Attachment
    override val thumbnail: Attachment?
    override val cover: Attachment?

    /**
     * Telegram [InputMediaAnimation](https://core.telegram.org/bots/api#inputmediaanimation) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Animation internal constructor(
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
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalRichCaption {
        override val type: Type = Type.ANIMATION
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Telegram [InputMediaAudio](https://core.telegram.org/bots/api#inputmediaaudio) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Audio internal constructor(
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val duration: Duration? = null,
        val performer: String? = null,
        val title: String? = null
    ) : InputMedia, Poll, RichMessage, HasOptionalRichCaption {
        override val type: Type = Type.AUDIO
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Telegram [InputMediaDocument](https://core.telegram.org/bots/api#inputmediadocument) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Document internal constructor(
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val disableContentTypeDetection: Boolean = false
    ) : InputMedia, Poll, HasOptionalRichCaption {
        override val type: Type = Type.DOCUMENT
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Telegram [InputMediaPhoto](https://core.telegram.org/bots/api#inputmediaphoto) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        override val media: Attachment,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalRichCaption {
        override val type: Type = Type.PHOTO
        override val thumbnail = null
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Telegram [InputMediaVideo](https://core.telegram.org/bots/api#inputmediavideo) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
        override val media: Attachment,
        override val thumbnail: Attachment? = null,
        override val cover: Attachment? = null,
        val startTimestamp: Duration? = null,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val width: Int? = null,
        val height: Int? = null,
        val duration: Duration? = null,
        val supportsStreaming: Boolean = false,
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalRichCaption {
        override val type: Type = Type.VIDEO

        companion object
    }

    /**
     * Telegram [InputMediaLivePhoto](https://core.telegram.org/bots/api#inputmedialivephoto) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class LivePhoto internal constructor(
        override val media: Attachment,
        val photo: Attachment,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val showCaptionAboveMedia: Boolean = false,
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, HasOptionalRichCaption {
        override val type: Type = Type.LIVE_PHOTO
        override val thumbnail: Attachment? = null
        override val cover: Attachment? = null

        companion object
    }

    @TelegramCodegen.Type
    data class VoiceNote internal constructor(
        override val media: Attachment,
        override val caption: String? = null,
        override val parseMode: RichText.ParseMode? = null,
        override val captionEntities: List<MessageEntity>? = null,
        val duration: Duration? = null
    ) : InputMedia, RichMessage, HasOptionalRichCaption {
        override val type: Type = Type.VOICE_NOTE
        override val thumbnail: Attachment? = null
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Telegram [InputSticker](https://core.telegram.org/bots/api#inputsticker) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Sticker internal constructor(
        val sticker: Attachment,
        val format: ski.gagar.vertigram.telegram.types.Sticker.Format,
        val emojiList: List<String>,
        val maskPosition: ski.gagar.vertigram.telegram.types.Sticker.MaskPosition? = null,
        val keywords: List<String>? = null
    ) {

        companion object
    }

    /**
     * Telegram [InputMediaSticker](https://core.telegram.org/bots/api#inputmediasticker) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class PollSticker internal constructor(
        val media: Attachment,
        val emoji: String? = null
    ) : PollOption {
        override val type: Type = Type.STICKER

        companion object
    }

    /**
     * Telegram [InputMediaLink](https://core.telegram.org/bots/api#inputmedialink) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Link internal constructor(
        val url: String
    ) : PollOption {
        override val type: Type = Type.LINK

        companion object
    }

    /**
     * Telegram [InputMediaLocation](https://core.telegram.org/bots/api#inputmedialocation) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Location internal constructor(
        val latitude: Double,
        val longitude: Double,
        val horizontalAccuracy: Double? = null
    ) : Poll, PollOption {
        override val type: Type = Type.LOCATION

        companion object
    }

    /**
     * Telegram [InputMediaVenue](https://core.telegram.org/bots/api#inputmediavenue) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Venue internal constructor(
        val latitude: Double,
        val longitude: Double,
        val title: String,
        val address: String,
        val foursquareId: String? = null,
        val foursquareType: String? = null,
        val googlePlaceId: String? = null,
        val googlePlaceType: String? = null
    ) : Poll, PollOption {
        override val type: Type = Type.VENUE

        companion object
    }

    /**
     * Telegram [InputProfilePhoto](https://core.telegram.org/bots/api#inputprofilephoto) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = ProfilePhoto.Static::class, name = ProfilePhoto.Type.STATIC_STR),
        JsonSubTypes.Type(value = ProfilePhoto.Animated::class, name = ProfilePhoto.Type.ANIMATED_STR)
    )
    sealed interface ProfilePhoto {
        val type: Type
        val attachment: Attachment

        /**
         * Telegram [InputProfilePhotoStatic](https://core.telegram.org/bots/api#inputprofilephotostatic) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Static internal constructor(
            val photo: Attachment
        ) : ProfilePhoto {
            override val type: Type = Type.STATIC
            @JsonIgnore
            override val attachment: Attachment = photo

            companion object
        }

        /**
         * Telegram [InputProfilePhotoAnimated](https://core.telegram.org/bots/api#inputprofilephotoanimated) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Animated internal constructor(
            val animation: Attachment,
            @Fractional
            val mainFrameTimestamp: Duration? = null,
        ) : ProfilePhoto {
            override val type: Type = Type.ANIMATED
            @JsonIgnore
            override val attachment: Attachment = animation

            companion object
        }

        enum class Type {
            @JsonProperty(STATIC_STR)
            STATIC,
            @JsonProperty(ANIMATED_STR)
            ANIMATED;

            companion object {
                const val STATIC_STR = "static"
                const val ANIMATED_STR = "animated"
            }
        }

        companion object
    }

    /**
     * Telegram [InputStoryContent](https://core.telegram.org/bots/api#inputstorycontent) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = StoryContent.Photo::class, name = StoryContent.Type.PHOTO_STR),
        JsonSubTypes.Type(value = StoryContent.Video::class, name = StoryContent.Type.VIDEO_STR)
    )
    sealed interface StoryContent {
        val type: Type
        val attachment: Attachment

        /**
         * Telegram [InputStoryContentPhoto](https://core.telegram.org/bots/api#inputstorycontentphoto) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Photo internal constructor(
            val photo: Attachment
        ) : StoryContent {
            override val type: Type = Type.PHOTO
            @JsonIgnore
            override val attachment: Attachment = photo

            companion object
        }

        /**
         * Telegram [InputStoryContentPhoto](https://core.telegram.org/bots/api#inputstorycontentphoto) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Video internal constructor(
            val video: Attachment,
            val duration: Duration? = null,
            val coverFrameTimestamp: Duration? = null,
            @get:JvmName("getIsAnimation")
            val isAnimation: Boolean = false
        ) : StoryContent {
            override val type: Type = Type.VIDEO
            @JsonIgnore
            override val attachment: Attachment = video

            companion object
        }

        /**
         * A value for [StoryContent.type] field.
         */
        enum class Type {
            @JsonProperty(PHOTO_STR)
            PHOTO,
            @JsonProperty(VIDEO_STR)
            VIDEO;

            companion object {
                const val PHOTO_STR = "photo"
                const val VIDEO_STR = "video"
            }
        }
    }

    /**
     * Telegram [InputPaidMedia](https://core.telegram.org/bots/api#inputpaidmedia) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Paid.LivePhoto::class, name = Paid.Type.LIVE_PHOTO_STR),
        JsonSubTypes.Type(value = Paid.Photo::class, name = Paid.Type.PHOTO_STR),
        JsonSubTypes.Type(value = Paid.Video::class, name = Paid.Type.VIDEO_STR)
    )
    sealed interface Paid : BaseInputMedia<Paid.Type> {
        override val type: Type
        override val media: Attachment
        override val thumbnail: Attachment?
        override val cover: Attachment?

        /**
         * Telegram [InputPaidMediaLivePhoto](https://core.telegram.org/bots/api#inputpaidmedialivephoto) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class LivePhoto internal constructor(
            override val media: Attachment,
            val photo: Attachment
        ) : Paid {
            override val type: Type = Type.LIVE_PHOTO
            override val thumbnail: Attachment? = null
            override val cover: Attachment? = null

            companion object
        }

        /**
         * Telegram [InputPaidMediaPhoto](https://core.telegram.org/bots/api#inputpaidmediaphoto) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Photo internal constructor(
            override val media: Attachment,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val showCaptionAboveMedia: Boolean = false,
            val hasSpoiler: Boolean = false
        ) : Paid, HasOptionalRichCaption {
            override val type: Type = Type.PHOTO
            override val thumbnail: Attachment? = null
            override val cover: Attachment? = null

            companion object
        }

        /**
         * Telegram [InputPaidMediaVideo](https://core.telegram.org/bots/api#inputpaidmediavideo) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Video internal constructor(
            override val media: Attachment,
            override val thumbnail: Attachment? = null,
            override val cover: Attachment? = null,
            val startTimestamp: Duration? = null,
            override val caption: String? = null,
            override val parseMode: RichText.ParseMode? = null,
            override val captionEntities: List<MessageEntity>? = null,
            val showCaptionAboveMedia: Boolean = false,
            val width: Int? = null,
            val height: Int? = null,
            val duration: Duration? = null,
            val supportsStreaming: Boolean = false,
            val hasSpoiler: Boolean = false

        ) : Paid, HasOptionalRichCaption{
            override val type: Type = Type.VIDEO
            companion object
        }


        /**
         * Value for [type]
         */
        enum class Type {
            @JsonProperty(LIVE_PHOTO_STR)
            LIVE_PHOTO,
            @JsonProperty(PHOTO_STR)
            PHOTO,
            @JsonProperty(VIDEO_STR)
            VIDEO;

            companion object {
                const val LIVE_PHOTO_STR = "live_photo"
                const val PHOTO_STR = "photo"
                const val VIDEO_STR = "video"
            }
        }
    }

    /**
     * Telegram [InputPollMedia](https://core.telegram.org/bots/api#inputpollmedia) type.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Animation::class, name = Type.ANIMATION_STR),
        JsonSubTypes.Type(value = Audio::class, name = Type.AUDIO_STR),
        JsonSubTypes.Type(value = Document::class, name = Type.DOCUMENT_STR),
        JsonSubTypes.Type(value = LivePhoto::class, name = Type.LIVE_PHOTO_STR),
        JsonSubTypes.Type(value = Location::class, name = Type.LOCATION_STR),
        JsonSubTypes.Type(value = Photo::class, name = Type.PHOTO_STR),
        JsonSubTypes.Type(value = Venue::class, name = Type.VENUE_STR),
        JsonSubTypes.Type(value = Video::class, name = Type.VIDEO_STR)
    )
    sealed interface Poll {
        val type: Type
    }

    /**
     * Telegram [InputPollOptionMedia](https://core.telegram.org/bots/api#inputpolloptionmedia) type.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Animation::class, name = Type.ANIMATION_STR),
        JsonSubTypes.Type(value = Link::class, name = Type.LINK_STR),
        JsonSubTypes.Type(value = LivePhoto::class, name = Type.LIVE_PHOTO_STR),
        JsonSubTypes.Type(value = Location::class, name = Type.LOCATION_STR),
        JsonSubTypes.Type(value = Photo::class, name = Type.PHOTO_STR),
        JsonSubTypes.Type(value = PollSticker::class, name = Type.STICKER_STR),
        JsonSubTypes.Type(value = Venue::class, name = Type.VENUE_STR),
        JsonSubTypes.Type(value = Video::class, name = Type.VIDEO_STR)
    )
    sealed interface PollOption {
        val type: Type
    }

    /**
     * Telegram media types accepted by [InputRichMessageMedia].
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Animation::class, name = Type.ANIMATION_STR),
        JsonSubTypes.Type(value = Audio::class, name = Type.AUDIO_STR),
        JsonSubTypes.Type(value = Photo::class, name = Type.PHOTO_STR),
        JsonSubTypes.Type(value = Video::class, name = Type.VIDEO_STR),
        JsonSubTypes.Type(value = VoiceNote::class, name = Type.VOICE_NOTE_STR)
    )
    sealed interface RichMessage {
        val type: Type
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
        DOCUMENT,
        @JsonProperty(LIVE_PHOTO_STR)
        LIVE_PHOTO,
        @JsonProperty(LOCATION_STR)
        LOCATION,
        @JsonProperty(STICKER_STR)
        STICKER,
        @JsonProperty(LINK_STR)
        LINK,
        @JsonProperty(VENUE_STR)
        VENUE,
        @JsonProperty(VOICE_NOTE_STR)
        VOICE_NOTE;

        companion object {
            const val PHOTO_STR = "photo"
            const val VIDEO_STR = "video"
            const val ANIMATION_STR = "animation"
            const val AUDIO_STR = "audio"
            const val DOCUMENT_STR = "document"
            const val LIVE_PHOTO_STR = "live_photo"
            const val LOCATION_STR = "location"
            const val STICKER_STR = "sticker"
            const val LINK_STR = "link"
            const val VENUE_STR = "venue"
            const val VOICE_NOTE_STR = "voice_note"
        }
    }
}
