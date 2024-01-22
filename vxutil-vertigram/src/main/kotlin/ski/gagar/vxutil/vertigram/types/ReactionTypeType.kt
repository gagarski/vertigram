package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class ReactionTypeType {
    @JsonProperty(EMOJI_STR)
    EMOJI,
    @JsonProperty(CUSTOM_EMOJI_STR)
    CUSTOM_EMOJI;

    companion object {
        const val EMOJI_STR = "emoji"
        const val CUSTOM_EMOJI_STR = "custom_emoji"
    }
}
