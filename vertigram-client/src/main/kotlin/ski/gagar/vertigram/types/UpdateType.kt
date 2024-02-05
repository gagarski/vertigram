package ski.gagar.vertigram.types

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
    @JsonProperty("message_reaction")
    MESSAGE_REACTION,
    @JsonProperty("message_reaction_COUNT")
    MESSAGE_REACTION_COUNT,
    @JsonProperty("inline_query")
    INLINE_QUERY,
    @JsonProperty("chosen_inline_result")
    CHOSEN_INLINE_RESULT,
    @JsonProperty("callback_query")
    CALLBACK_QUERY,
    @JsonProperty("shipping_query")
    SHIPPING_QUERY,
    @JsonProperty("pre_checkout_query")
    PRE_CHECKOUT_QUERY,
    @JsonProperty("poll")
    POLL,
    @JsonProperty("poll_answer")
    POLL_ANSWER,
    @JsonProperty("my_chat_member")
    MY_CHAT_MEMBER,
    @JsonProperty("chat_member")
    CHAT_MEMBER,
    @JsonProperty("chat_join_request")
    CHAT_JOIN_REQUEST;

    override fun toString(): String = super.toString().lowercase()

}
