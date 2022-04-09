package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Available values for [PassportElementError.source]
 */
enum class PassportElementErrorSource {
    @JsonProperty(DATA_FIELD_STR)
    DATA_FIELD,
    @JsonProperty(FRONT_SIDE_STR)
    FRONT_SIDE,
    @JsonProperty(REVERSE_SIDE_STR)
    REVERSE_SIDE,
    @JsonProperty(SELFIE_STR)
    SELFIE,
    @JsonProperty(FILE_STR)
    FILE,
    @JsonProperty(FILES_STR)
    FILES,
    @JsonProperty(TRANSLATION_FILE_STR)
    TRANSLATION_FILE,
    @JsonProperty(TRANSLATION_FILES_STR)
    TRANSLATION_FILES,
    @JsonProperty(UNSPECIFIED_STR)
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
