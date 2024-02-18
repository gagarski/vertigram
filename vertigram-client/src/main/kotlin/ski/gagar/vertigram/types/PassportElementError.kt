package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "source", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = PassportElementErrorDataField::class, name = PassportElementErrorSource.DATA_FIELD_STR),
    JsonSubTypes.Type(value = PassportElementErrorFrontSide::class, name = PassportElementErrorSource.FRONT_SIDE_STR),
    JsonSubTypes.Type(value = PassportElementErrorReverseSide::class, name = PassportElementErrorSource.REVERSE_SIDE_STR),
    JsonSubTypes.Type(value = PassportElementErrorSelfie::class, name = PassportElementErrorSource.SELFIE_STR),
    JsonSubTypes.Type(value = PassportElementErrorFile::class, name = PassportElementErrorSource.FILE_STR),
    JsonSubTypes.Type(value = PassportElementErrorFiles::class, name = PassportElementErrorSource.FILES_STR),
    JsonSubTypes.Type(value = PassportElementErrorTranslationFile::class, name = PassportElementErrorSource.TRANSLATION_FILE_STR),
    JsonSubTypes.Type(value = PassportElementErrorTranslationFiles::class, name = PassportElementErrorSource.TRANSLATION_FILES_STR),
    JsonSubTypes.Type(value = PassportElementErrorUnspecified::class, name = PassportElementErrorSource.UNSPECIFIED_STR),
)
sealed interface PassportElementError {
    val source: PassportElementErrorSource
}

