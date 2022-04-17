package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class UpdateType {
    @JsonProperty("message")
    MESSAGE,
    @JsonProperty("edited_message")
    EDITED_MESSAGE,
    @JsonProperty("channel_post")
    CHANNEL_POST,
    @JsonProperty("edited_channel_post")
    EDITED_CHANNEL_POST,
    @JsonProperty("inline_query")
    INLINE_QUERY,
    @JsonProperty("chosen_inline_result")
    CHOSEN_INLINE_RESULT,
    @JsonProperty("callback_query")
    CALLBACK_QUERY,
    @JsonProperty("shipping_query")
    SHIPPING_QUERY,
    @JsonProperty("pre_checkout_query")
    PRE_CHECKOUT_QUERY;

    override fun toString(): String = super.toString().lowercase()

}
