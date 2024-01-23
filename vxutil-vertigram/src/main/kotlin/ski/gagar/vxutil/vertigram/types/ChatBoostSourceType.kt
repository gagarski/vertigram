package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class ChatBoostSourceType {
    @JsonProperty(PREMIUM_STR)
    PREMIUM,
    @JsonProperty(GIFT_CODE_STR)
    GIFT_CODE,
    @JsonProperty(GIVEAWAY_STR)
    GIVEAWAY;

    companion object {
        const val PREMIUM_STR = "premium"
        const val GIFT_CODE_STR = "gift_code"
        const val GIVEAWAY_STR = "giveaway"
    }
}
