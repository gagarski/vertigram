package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vxutil.vertigram.types.attachments.Attachment

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = InputMediaPhoto::class, name = InputMediaType.PHOTO_STR),
    JsonSubTypes.Type(value = InputMediaVideo::class, name = InputMediaType.VIDEO_STR),
    JsonSubTypes.Type(value = InputMediaAnimation::class, name = InputMediaType.ANIMATION_STR),
    JsonSubTypes.Type(value = InputMediaAudio::class, name = InputMediaType.AUDIO_STR),
    JsonSubTypes.Type(value = InputMediaDocument::class, name = InputMediaType.DOCUMENT_STR),
)
sealed interface InputMedia {
    val type: InputMediaType
    val media: Attachment
    val thumb: Attachment?
    fun instantiate(media: Attachment, thumb: Attachment?): InputMedia
}
