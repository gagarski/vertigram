package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Sticker](https://core.telegram.org/bots/api#sticker) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class Sticker internal constructor(
    val fileId: String,
    val fileUniqueId: String,
    val type: Type,
    val width: Int,
    val height: Int,
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    val thumbnail: PhotoSize? = null,
    val emoji: String? = null,
    val setName: String? = null,
    val premiumAnimation: File? = null,
    val maskPosition: MaskPosition? = null,
    val customEmojiId: String? = null,
    val needsRepainting: Boolean = false,
    val fileSize: Long? = null
) {
    /**
     * Telegram [MaskPosition](https://core.telegram.org/bots/api#maskposition) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class MaskPosition internal constructor(
        val point: String,
        val xShift: Double,
        val yShift: Double,
        val scale: Double
    ) {
        companion object
    }

    /**
     * Sticker format as used in [ski.gagar.vertigram.telegram.methods.CreateNewStickerSet] and [ski.gagar.vertigram.telegram.methods.UploadStickerFile]
     * methods.
     */
    enum class Format {
        @JsonProperty("static")
        STATIC,
        @JsonProperty("animated")
        ANIMATED,
        @JsonProperty("video")
        VIDEO,
    }

    /**
     * Value for [type] field
     */
    enum class Type {
        @JsonProperty("regular")
        REGULAR,
        @JsonProperty("mask")
        MASK,
        @JsonProperty("custom_emoji")
        CUSTOM_EMOJI,
    }

    companion object
}
