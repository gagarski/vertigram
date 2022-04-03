package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Available values for [PassportElementError.source]
 */
enum class PassportElementErrorSource {
    @TgEnumName(DATA_FIELD_STR)
    DATA_FIELD,
    @TgEnumName(FRONT_SIDE_STR)
    FRONT_SIDE,
    @TgEnumName(REVERSE_SIDE_STR)
    REVERSE_SIDE,
    @TgEnumName(SELFIE_STR)
    SELFIE,
    @TgEnumName(FILE_STR)
    FILE,
    @TgEnumName(FILES_STR)
    FILES,
    @TgEnumName(TRANSLATION_FILE_STR)
    TRANSLATION_FILE,
    @TgEnumName(TRANSLATION_FILES_STR)
    TRANSLATION_FILES,
    @TgEnumName(UNSPECIFIED_STR)
    UNSPECIFIED;
    companion object {
        const val DATA_FIELD_STR = "data"
        const val FRONT_SIDE_STR = "front_side"
        const val REVERSE_SIDE_STR = "reverse_side"
        const val SELFIE_STR = "selfie"
        const val FILE_STR = "file"
        const val FILES_STR = "files"
        const val TRANSLATION_FILE_STR = "translation_file"
        const val TRANSLATION_FILES_STR = "translation_files"
        const val UNSPECIFIED_STR = "unspecified"
    }
}
