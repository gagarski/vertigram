package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Describes paid media.
 *
 * See Telegram's [PaidMedia](https://core.telegram.org/bots/api#paidmedia) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = PaidMedia.Preview::class, name = PaidMedia.Type.PREVIEW_STR),
    JsonSubTypes.Type(value = PaidMedia.LivePhoto::class, name = PaidMedia.Type.LIVE_PHOTO_STR),
    JsonSubTypes.Type(value = PaidMedia.Photo::class, name = PaidMedia.Type.PHOTO_STR),
    JsonSubTypes.Type(value = PaidMedia.Video::class, name = PaidMedia.Type.VIDEO_STR),
)
sealed interface PaidMedia {
    val type: Type

    /**
     * Case when the paid media isn't available before payment.
     *
     * See Telegram's [PaidMediaPreview](https://core.telegram.org/bots/api#paidmediapreview) documentation.
     */
    @TelegramCodegen.Type
    data class Preview internal constructor(
        /** Media width. */
        val width: Int? = null,
        /** Media height. */
        val height: Int? = null,
        /** Media duration in seconds. */
        val duration: Int? = null,
    ) : PaidMedia {
        override val type: Type = Type.PREVIEW

        companion object
    }

    /**
     * Case when the paid media is a live photo.
     *
     * See Telegram's [PaidMediaLivePhoto](https://core.telegram.org/bots/api#paidmedialivephoto) documentation.
     */
    @TelegramCodegen.Type
    data class LivePhoto internal constructor(
        /** Live photo. */
        val livePhoto: ski.gagar.vertigram.telegram.types.LivePhoto
    ) : PaidMedia {
        override val type: Type = Type.LIVE_PHOTO

        companion object
    }

    /**
     * Case when the paid media is a photo.
     *
     * See Telegram's [PaidMediaPhoto](https://core.telegram.org/bots/api#paidmediaphoto) documentation.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        /** Available sizes of the photo. */
        val photo: List<PhotoSize>
    ) : PaidMedia {
        override val type: Type = Type.PHOTO

        companion object
    }

    /**
     * Case when the paid media is a video.
     *
     * See Telegram's [PaidMediaVideo](https://core.telegram.org/bots/api#paidmediavideo) documentation.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
        /** Video. */
        val video: ski.gagar.vertigram.telegram.types.Video
    ) : PaidMedia {
        override val type: Type = Type.VIDEO

        companion object
    }

    /**
     * Value for [type]
     */
    enum class Type {
        @JsonProperty(PREVIEW_STR)
        PREVIEW,
        @JsonProperty(LIVE_PHOTO_STR)
        LIVE_PHOTO,
        @JsonProperty(PHOTO_STR)
        PHOTO,
        @JsonProperty(VIDEO_STR)
        VIDEO;
        companion object {
            const val PREVIEW_STR = "preview"
            const val LIVE_PHOTO_STR = "live_photo"
            const val PHOTO_STR = "photo"
            const val VIDEO_STR = "video"
        }
    }
}
