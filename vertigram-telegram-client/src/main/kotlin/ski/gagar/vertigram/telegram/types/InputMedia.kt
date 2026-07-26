package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalFormattedCaption
import ski.gagar.vertigram.telegram.types.formattedtext.FormattedText
import ski.gagar.vertigram.util.json.annotations.Fractional
import java.time.Duration

interface BaseInputMedia<T> {
    val type: T
    val media: Attachment
    val thumbnail: Attachment?
    val cover: Attachment?
}

/**
 * Represents the content of a media message to be sent.
 *
 * Telegram's `InputMediaXxx` types are represented as nested `InputMedia.Xxx` types. [Sticker] represents Telegram's
 * `InputSticker` and isn't an [InputMedia] subtype.
 *
 * See Telegram's [InputMedia](https://core.telegram.org/bots/api#inputmedia) documentation.
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
     * Case when the media is an animation file: a GIF or H.264/MPEG-4 AVC video without sound.
     *
     * See Telegram's [InputMediaAnimation](https://core.telegram.org/bots/api#inputmediaanimation) documentation.
     */
    @TelegramCodegen.Type
    data class Animation internal constructor(
        /** Animation file to send. */
        override val media: Attachment,
        /** JPEG thumbnail of the uploaded file, less than 200 kB and at most 320x320. */
        override val thumbnail: Attachment? = null,
        /** Caption of the animation, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Whether the caption must be shown above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** Animation width. */
        val width: Int? = null,
        /** Animation height. */
        val height: Int? = null,
        /** Animation duration. */
        val duration: Duration? = null,
        /** Whether the animation needs to be covered with a spoiler animation. */
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalFormattedCaption {
        override val type: Type = Type.ANIMATION
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Case when the media is an audio file to be treated as music.
     *
     * See Telegram's [InputMediaAudio](https://core.telegram.org/bots/api#inputmediaaudio) documentation.
     */
    @TelegramCodegen.Type
    data class Audio internal constructor(
        /** Audio file to send. */
        override val media: Attachment,
        /** JPEG thumbnail of the uploaded file, less than 200 kB and at most 320x320. */
        override val thumbnail: Attachment? = null,
        /** Caption of the audio, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Duration of the audio. */
        val duration: Duration? = null,
        /** Performer of the audio. */
        val performer: String? = null,
        /** Title of the audio. */
        val title: String? = null
    ) : InputMedia, Poll, RichMessage, HasOptionalFormattedCaption {
        override val type: Type = Type.AUDIO
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Case when the media is a general file.
     *
     * See Telegram's [InputMediaDocument](https://core.telegram.org/bots/api#inputmediadocument) documentation.
     */
    @TelegramCodegen.Type
    data class Document internal constructor(
        /** File to send. */
        override val media: Attachment,
        /** JPEG thumbnail of the uploaded file, less than 200 kB and at most 320x320. */
        override val thumbnail: Attachment? = null,
        /** Caption of the document, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /**
         * Whether to disable automatic server-side content type detection for uploaded files. Always enabled when the
         * document is sent as part of an album.
         */
        val disableContentTypeDetection: Boolean = false
    ) : InputMedia, Poll, HasOptionalFormattedCaption {
        override val type: Type = Type.DOCUMENT
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Case when the media is a photo.
     *
     * See Telegram's [InputMediaPhoto](https://core.telegram.org/bots/api#inputmediaphoto) documentation.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        /** Photo to send. */
        override val media: Attachment,
        /** Caption of the photo, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Whether the caption must be shown above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** Whether the photo needs to be covered with a spoiler animation. */
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalFormattedCaption {
        override val type: Type = Type.PHOTO
        override val thumbnail = null
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Case when the media is a video.
     *
     * See Telegram's [InputMediaVideo](https://core.telegram.org/bots/api#inputmediavideo) documentation.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
        /** Video file to send. */
        override val media: Attachment,
        /** JPEG thumbnail of the uploaded file, less than 200 kB and at most 320x320. */
        override val thumbnail: Attachment? = null,
        /** Cover for the video in the message. */
        override val cover: Attachment? = null,
        /** Start timestamp for the video in the message. */
        val startTimestamp: Duration? = null,
        /** Caption of the video, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Whether the caption must be shown above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** Video width. */
        val width: Int? = null,
        /** Video height. */
        val height: Int? = null,
        /** Video duration. */
        val duration: Duration? = null,
        /** Whether the uploaded video is suitable for streaming. */
        val supportsStreaming: Boolean = false,
        /** Whether the video needs to be covered with a spoiler animation. */
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, RichMessage, HasOptionalFormattedCaption {
        override val type: Type = Type.VIDEO

        companion object
    }

    /**
     * Case when the media is a live photo.
     *
     * Sending live photos by URL is currently unsupported.
     *
     * See Telegram's [InputMediaLivePhoto](https://core.telegram.org/bots/api#inputmedialivephoto) documentation.
     */
    @TelegramCodegen.Type
    data class LivePhoto internal constructor(
        /** Video of the live photo to send. */
        override val media: Attachment,
        /** Static photo to send. */
        val photo: Attachment,
        /** Caption of the live photo, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Whether the caption must be shown above the message media. */
        val showCaptionAboveMedia: Boolean = false,
        /** Whether the live photo needs to be covered with a spoiler animation. */
        val hasSpoiler: Boolean = false
    ) : InputMedia, Poll, PollOption, HasOptionalFormattedCaption {
        override val type: Type = Type.LIVE_PHOTO
        override val thumbnail: Attachment? = null
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Case when the media is a voice message file.
     *
     * See Telegram's [InputMediaVoiceNote](https://core.telegram.org/bots/api#inputmediavoicenote) documentation.
     */
    @TelegramCodegen.Type
    data class VoiceNote internal constructor(
        /** Voice message file to send. */
        override val media: Attachment,
        /** Caption of the voice message, 0-1024 characters after entities parsing. */
        override val caption: String? = null,
        /** Mode for parsing entities in [caption]. */
        override val parseMode: FormattedText.ParseMode? = null,
        /** Special entities that appear in [caption], specified instead of [parseMode]. */
        override val captionEntities: List<MessageEntity>? = null,
        /** Duration of the voice message. */
        val duration: Duration? = null
    ) : InputMedia, RichMessage, HasOptionalFormattedCaption {
        override val type: Type = Type.VOICE_NOTE
        override val thumbnail: Attachment? = null
        override val cover: Attachment? = null

        companion object
    }

    /**
     * Describes a sticker to be added to a sticker set.
     *
     * See Telegram's [InputSticker](https://core.telegram.org/bots/api#inputsticker) documentation.
     */
    @TelegramCodegen.Type
    data class Sticker internal constructor(
        /** Sticker to add. */
        val sticker: Attachment,
        /** Format of the added sticker. */
        val format: ski.gagar.vertigram.telegram.types.Sticker.Format,
        /** One or more emoji associated with the sticker. */
        val emojiList: List<String>,
        /** Position where the mask should be placed on faces. */
        val maskPosition: ski.gagar.vertigram.telegram.types.Sticker.MaskPosition? = null,
        /** Search keywords for the sticker, up to 20 keywords with up to 64 total characters. */
        val keywords: List<String>? = null
    ) {

        companion object
    }

    /**
     * Case when poll option media is a sticker.
     *
     * See Telegram's [InputMediaSticker](https://core.telegram.org/bots/api#inputmediasticker) documentation.
     */
    @TelegramCodegen.Type
    data class PollSticker internal constructor(
        /** Sticker to send. */
        val media: Attachment,
        /** Emoji associated with the sticker. */
        val emoji: String? = null
    ) : PollOption {
        override val type: Type = Type.STICKER

        companion object
    }

    /**
     * Case when poll option media is an HTTP link.
     *
     * See Telegram's [InputMediaLink](https://core.telegram.org/bots/api#inputmedialink) documentation.
     */
    @TelegramCodegen.Type
    data class Link internal constructor(
        /** HTTP URL of the link. */
        val url: String
    ) : PollOption {
        override val type: Type = Type.LINK

        companion object
    }

    /**
     * Case when the media is a location.
     *
     * See Telegram's [InputMediaLocation](https://core.telegram.org/bots/api#inputmedialocation) documentation.
     */
    @TelegramCodegen.Type
    data class Location internal constructor(
        /** Latitude of the location. */
        val latitude: Double,
        /** Longitude of the location. */
        val longitude: Double,
        /** Radius of uncertainty for the location in meters; 0-1500. */
        val horizontalAccuracy: Double? = null
    ) : Poll, PollOption {
        override val type: Type = Type.LOCATION

        companion object
    }

    /**
     * Case when the media is a venue.
     *
     * See Telegram's [InputMediaVenue](https://core.telegram.org/bots/api#inputmediavenue) documentation.
     */
    @TelegramCodegen.Type
    data class Venue internal constructor(
        /** Latitude of the venue. */
        val latitude: Double,
        /** Longitude of the venue. */
        val longitude: Double,
        /** Name of the venue. */
        val title: String,
        /** Address of the venue. */
        val address: String,
        /** Foursquare identifier of the venue, if known. */
        val foursquareId: String? = null,
        /** Foursquare type of the venue, if known. */
        val foursquareType: String? = null,
        /** Google Places identifier of the venue. */
        val googlePlaceId: String? = null,
        /** Google Places type of the venue. */
        val googlePlaceType: String? = null
    ) : Poll, PollOption {
        override val type: Type = Type.VENUE

        companion object
    }

    /**
     * Describes a profile photo to set.
     *
     * See Telegram's [InputProfilePhoto](https://core.telegram.org/bots/api#inputprofilephoto) documentation.
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
         * Case when the profile photo is a static photo in JPEG format.
         *
         * See Telegram's [InputProfilePhotoStatic](https://core.telegram.org/bots/api#inputprofilephotostatic)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Static internal constructor(
            /** Static profile photo. It can't be reused and can only be uploaded as a new file. */
            val photo: Attachment
        ) : ProfilePhoto {
            override val type: Type = Type.STATIC
            @JsonIgnore
            override val attachment: Attachment = photo

            companion object
        }

        /**
         * Case when the profile photo is animated in MPEG-4 format.
         *
         * See Telegram's [InputProfilePhotoAnimated](https://core.telegram.org/bots/api#inputprofilephotoanimated)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Animated internal constructor(
            /** Animated profile photo. It can't be reused and can only be uploaded as a new file. */
            val animation: Attachment,
            /** Timestamp of the frame to use as the static profile photo. */
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
     * Describes the content of a story to post.
     *
     * See Telegram's [InputStoryContent](https://core.telegram.org/bots/api#inputstorycontent) documentation.
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
         * Case when the story content is a photo.
         *
         * See Telegram's [InputStoryContentPhoto](https://core.telegram.org/bots/api#inputstorycontentphoto)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Photo internal constructor(
            /**
             * Photo to post. It must be 1080x1920, must not exceed 10 MB, and can only be uploaded as a new file.
             */
            val photo: Attachment
        ) : StoryContent {
            override val type: Type = Type.PHOTO
            @JsonIgnore
            override val attachment: Attachment = photo

            companion object
        }

        /**
         * Case when the story content is a video.
         *
         * See Telegram's [InputStoryContentVideo](https://core.telegram.org/bots/api#inputstorycontentvideo)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Video internal constructor(
            /**
             * Video to post. It must be 720x1280, streamable, H.265-encoded MPEG-4 with key frames each second, no
             * larger than 30 MB, and can only be uploaded as a new file.
             */
            val video: Attachment,
            /** Precise duration of the video; 0-60 seconds. */
            val duration: Duration? = null,
            /** Timestamp of the frame to use as the static cover for the story. */
            val coverFrameTimestamp: Duration? = null,
            /** Whether the video has no sound. */
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
     * Describes paid media to be sent.
     *
     * See Telegram's [InputPaidMedia](https://core.telegram.org/bots/api#inputpaidmedia) documentation.
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
         * Case when the paid media to send is a live photo.
         *
         * Sending live photos by URL is currently unsupported.
         *
         * See Telegram's [InputPaidMediaLivePhoto](https://core.telegram.org/bots/api#inputpaidmedialivephoto)
         * documentation.
         */
        @TelegramCodegen.Type
        data class LivePhoto internal constructor(
            /** Video of the live photo to send. */
            override val media: Attachment,
            /** Static photo to send. */
            val photo: Attachment
        ) : Paid {
            override val type: Type = Type.LIVE_PHOTO
            override val thumbnail: Attachment? = null
            override val cover: Attachment? = null

            companion object
        }

        /**
         * Case when the paid media to send is a photo.
         *
         * See Telegram's [InputPaidMediaPhoto](https://core.telegram.org/bots/api#inputpaidmediaphoto) documentation.
         */
        @TelegramCodegen.Type
        data class Photo internal constructor(
            /** Photo to send. */
            override val media: Attachment,
            /** Caption of the photo, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Whether the photo needs to be covered with a spoiler animation. */
            val hasSpoiler: Boolean = false
        ) : Paid, HasOptionalFormattedCaption {
            override val type: Type = Type.PHOTO
            override val thumbnail: Attachment? = null
            override val cover: Attachment? = null

            companion object
        }

        /**
         * Case when the paid media to send is a video.
         *
         * See Telegram's [InputPaidMediaVideo](https://core.telegram.org/bots/api#inputpaidmediavideo) documentation.
         */
        @TelegramCodegen.Type
        data class Video internal constructor(
            /** Video file to send. */
            override val media: Attachment,
            /** JPEG thumbnail of the uploaded file, less than 200 kB and at most 320x320. */
            override val thumbnail: Attachment? = null,
            /** Cover for the video in the message. */
            override val cover: Attachment? = null,
            /** Start timestamp for the video in the message. */
            val startTimestamp: Duration? = null,
            /** Caption of the video, 0-1024 characters after entities parsing. */
            override val caption: String? = null,
            /** Mode for parsing entities in [caption]. */
            override val parseMode: FormattedText.ParseMode? = null,
            /** Special entities that appear in [caption], specified instead of [parseMode]. */
            override val captionEntities: List<MessageEntity>? = null,
            /** Whether the caption must be shown above the message media. */
            val showCaptionAboveMedia: Boolean = false,
            /** Video width. */
            val width: Int? = null,
            /** Video height. */
            val height: Int? = null,
            /** Video duration. */
            val duration: Duration? = null,
            /** Whether the uploaded video is suitable for streaming. */
            val supportsStreaming: Boolean = false,
            /** Whether the video needs to be covered with a spoiler animation. */
            val hasSpoiler: Boolean = false

        ) : Paid, HasOptionalFormattedCaption{
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
     * Represents the content of a poll description or quiz explanation to be sent.
     *
     * See Telegram's [InputPollMedia](https://core.telegram.org/bots/api#inputpollmedia) documentation.
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
     * Represents the content of a poll option to be sent.
     *
     * See Telegram's [InputPollOptionMedia](https://core.telegram.org/bots/api#inputpolloptionmedia) documentation.
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

    /** Media types accepted by [InputRichMessageMedia]. */
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
