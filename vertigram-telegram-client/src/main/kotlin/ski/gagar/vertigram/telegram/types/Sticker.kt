package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Represents a sticker.
 *
 * See Telegram's [Sticker](https://core.telegram.org/bots/api#sticker) documentation.
 */
@TelegramCodegen.Type
data class Sticker internal constructor(
    /** Identifier for downloading or reusing this file. */
    val fileId: String,
    /** Unique identifier for this file. */
    val fileUniqueId: String,
    /** Type of the sticker. */
    val type: Type,
    /** Sticker width. */
    val width: Int,
    /** Sticker height. */
    val height: Int,
    /** Whether the sticker is animated. */
    @get:JvmName("getIsAnimated")
    val isAnimated: Boolean = false,
    /** Whether the sticker is a video sticker. */
    @get:JvmName("getIsVideo")
    val isVideo: Boolean = false,
    /** Sticker thumbnail. */
    val thumbnail: PhotoSize? = null,
    /** Emoji associated with the sticker. */
    val emoji: String? = null,
    /** Name of the sticker set to which the sticker belongs. */
    val setName: String? = null,
    /** Premium animation for the sticker. */
    val premiumAnimation: File? = null,
    /** Position where the mask should be placed on faces. */
    val maskPosition: MaskPosition? = null,
    /** Unique identifier of the custom emoji. */
    val customEmojiId: String? = null,
    /** Whether the sticker must be repainted to a text color in messages. */
    val needsRepainting: Boolean = false,
    /** File size in bytes. */
    val fileSize: Long? = null
) {
    /**
     * Describes the position on faces where a mask should be placed.
     *
     * See Telegram's [MaskPosition](https://core.telegram.org/bots/api#maskposition) documentation.
     */
    @TelegramCodegen.Type
    data class MaskPosition internal constructor(
        /** Part of the face relative to which the mask should be placed. */
        val point: String,
        /** Shift by X-axis measured in mask widths. */
        val xShift: Double,
        /** Shift by Y-axis measured in mask heights. */
        val yShift: Double,
        /** Mask scaling coefficient. */
        val scale: Double
    ) {
        companion object
    }

    /**
     * Sticker format as used in [ski.gagar.vertigram.telegram.types.methods.CreateNewStickerSet] and
     * [ski.gagar.vertigram.telegram.types.methods.UploadStickerFile] methods.
     */
    enum class Format {
        /** Case for a static `.WEBP` or `.PNG` sticker. */
        @JsonProperty("static")
        STATIC,
        /** Case for an animated `.TGS` sticker. */
        @JsonProperty("animated")
        ANIMATED,
        /** Case for a video `.WEBM` sticker. */
        @JsonProperty("video")
        VIDEO,
    }

    /** Sticker type as returned in [type]. */
    enum class Type {
        /** Case for a regular sticker. */
        @JsonProperty("regular")
        REGULAR,
        /** Case for a mask sticker. */
        @JsonProperty("mask")
        MASK,
        /** Case for a custom emoji sticker. */
        @JsonProperty("custom_emoji")
        CUSTOM_EMOJI,
    }

    companion object
}
