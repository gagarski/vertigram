package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class StickerType {
    @JsonProperty("regular")
    REGULAR,
    @JsonProperty("mask")
    MASK,
    @JsonProperty("custom_emoji")
    CUSTOM_EMOJI,
}
