package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [Sticker](https://core.telegram.org/bots/api#sticker) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Sticker(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
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
    data class MaskPosition(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val point: String,
        val xShift: Double,
        val yShift: Double,
        val scale: Double
    )

    /**
     * Sticker format as used in [ski.gagar.vertigram.methods.CreateNewStickerSet] and [ski.gagar.vertigram.methods.UploadStickerFile]
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

}
