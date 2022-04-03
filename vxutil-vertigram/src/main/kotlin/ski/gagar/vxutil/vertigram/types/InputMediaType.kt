package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [InputMedia.type]
 */
enum class InputMediaType {
    @TgEnumName(PHOTO_STR)
    PHOTO,
    @TgEnumName(VIDEO_STR)
    VIDEO,
    @TgEnumName(ANIMATION_STR)
    ANIMATION,
    @TgEnumName(AUDIO_STR)
    AUDIO,
    @TgEnumName(DOCUMENT_STR)
    DOCUMENT;

    companion object {
        const val PHOTO_STR = "photo"
        const val VIDEO_STR = "video"
        const val ANIMATION_STR = "animation"
        const val AUDIO_STR = "audio"
        const val DOCUMENT_STR = "document"
    }
}
