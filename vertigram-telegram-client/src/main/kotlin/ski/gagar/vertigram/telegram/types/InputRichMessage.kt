package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen

/**
 * Telegram [InputRichMessage](https://core.telegram.org/bots/api#inputrichmessage) type.
 *
 * Subclasses represent the two mutually exclusive input formats.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = InputRichMessage.Html::class),
    JsonSubTypes.Type(value = InputRichMessage.Markdown::class)
)
sealed interface InputRichMessage {
    val isRtl: Boolean
    val skipEntityDetection: Boolean

    /**
     * Telegram [InputRichMessage](https://core.telegram.org/bots/api#inputrichmessage) with HTML content.
     */
    @TelegramCodegen.Type
    data class Html internal constructor(
        val html: String,
        @get:JvmName("getIsRtl")
        override val isRtl: Boolean = false,
        override val skipEntityDetection: Boolean = false
    ) : InputRichMessage {
        companion object
    }

    /**
     * Telegram [InputRichMessage](https://core.telegram.org/bots/api#inputrichmessage) with Markdown content.
     */
    @TelegramCodegen.Type
    data class Markdown internal constructor(
        val markdown: String,
        @get:JvmName("getIsRtl")
        override val isRtl: Boolean = false,
        override val skipEntityDetection: Boolean = false
    ) : InputRichMessage {
        companion object
    }
}
