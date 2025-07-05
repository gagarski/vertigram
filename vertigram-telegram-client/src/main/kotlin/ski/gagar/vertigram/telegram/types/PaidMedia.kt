package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [PaidMedia](https://core.telegram.org/bots/api#paidmedia) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = PaidMedia.Preview::class, name = PaidMedia.Type.PREVIEW_STR),
    JsonSubTypes.Type(value = PaidMedia.Photo::class, name = PaidMedia.Type.PHOTO_STR),
    JsonSubTypes.Type(value = PaidMedia.Video::class, name = PaidMedia.Type.VIDEO_STR),
)
sealed interface PaidMedia {
    val type: Type

    /**
     * Telegram [PaidMediaPreview](https://core.telegram.org/bots/api#paidmediapreview) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Preview internal constructor(
        val width: Int? = null,
        val height: Int? = null,
        val duration: Int? = null,
    ) : PaidMedia {
        override val type: Type = Type.PREVIEW

        companion object
    }

    /**
     * Telegram [PaidMediaPhoto](https://core.telegram.org/bots/api#paidmediaphoto) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Photo internal constructor(
        val photo: List<PhotoSize>
    ) : PaidMedia {
        override val type: Type = Type.PHOTO

        companion object
    }

    /**
     * Telegram [PaidMediaVideo](https://core.telegram.org/bots/api#paidmediavideo) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Video internal constructor(
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
        @JsonProperty(PHOTO_STR)
        PHOTO,
        @JsonProperty(VIDEO_STR)
        VIDEO;
        companion object {
            const val PREVIEW_STR = "preview"
            const val PHOTO_STR = "photo"
            const val VIDEO_STR = "video"
        }
    }
}
