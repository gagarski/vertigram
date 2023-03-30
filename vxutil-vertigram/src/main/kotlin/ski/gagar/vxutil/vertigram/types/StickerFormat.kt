package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class StickerFormat {
    @JsonProperty("static")
    STATIC,
    @JsonProperty("animated")
    ANIMATED,
    @JsonProperty("video")
    VIDEO,
}
