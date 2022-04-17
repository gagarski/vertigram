package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class InputMediaType {
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
