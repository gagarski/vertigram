package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [InlineQueryResult.type]
 */
enum class InlineQueryResultType {
    @TgEnumName(ARTICLE_STR)
    ARTICLE,
    @TgEnumName(PHOTO_STR)
    PHOTO,
    @TgEnumName(GIF_STR)
    GIF,
    @TgEnumName(MPEG4_GIF_STR)
    MPEG4_GIF,
    @TgEnumName(VIDEO_STR)
    VIDEO,
    @TgEnumName(AUDIO_STR)
    AUDIO,
    @TgEnumName(VOICE_STR)
    VOICE,
    @TgEnumName(DOCUMENT_STR)
    DOCUMENT,
    @TgEnumName(LOCATION_STR)
    LOCATION,
    @TgEnumName(VENUE_STR)
    VENUE,
    @TgEnumName(CONTACT_STR)
    CONTACT,
    @TgEnumName(GAME_STR)
    GAME,
    @TgEnumName(STICKER_STR)
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
