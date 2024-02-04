package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class InlineQueryResultType {
    @JsonProperty(ARTICLE_STR)
    ARTICLE,
    @JsonProperty(PHOTO_STR)
    PHOTO,
    @JsonProperty(GIF_STR)
    GIF,
    @JsonProperty(MPEG4_GIF_STR)
    MPEG4_GIF,
    @JsonProperty(VIDEO_STR)
    VIDEO,
    @JsonProperty(AUDIO_STR)
    AUDIO,
    @JsonProperty(VOICE_STR)
    VOICE,
    @JsonProperty(DOCUMENT_STR)
    DOCUMENT,
    @JsonProperty(LOCATION_STR)
    LOCATION,
    @JsonProperty(VENUE_STR)
    VENUE,
    @JsonProperty(CONTACT_STR)
    CONTACT,
    @JsonProperty(GAME_STR)
    GAME,
    @JsonProperty(STICKER_STR)
    STICKER;

    companion object {
        const val ARTICLE_STR = "article"
        const val PHOTO_STR = "photo"
        const val GIF_STR = "gif"
        const val MPEG4_GIF_STR = "mpeg4_gif"
        const val VIDEO_STR = "video"
        const val AUDIO_STR = "audio"
        const val VOICE_STR = "voice"
        const val DOCUMENT_STR = "document"
        const val LOCATION_STR = "location"
        const val VENUE_STR = "venue"
        const val CONTACT_STR = "contact"
        const val GAME_STR = "game"
        const val STICKER_STR = "sticker"
    }
}
