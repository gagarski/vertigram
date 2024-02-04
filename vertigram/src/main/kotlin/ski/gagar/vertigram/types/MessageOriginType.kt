package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class MessageOriginType {
    @JsonProperty(USER_STR)
    USER,
    @JsonProperty(HIDDEN_USER_STR)
    HIDDEN_USER,
    @JsonProperty(CHAT_STR)
    CHAT,
    @JsonProperty(CHANNEL_STR)
    CHANNEL,
    ;

    companion object {
        const val USER_STR = "user"
        const val HIDDEN_USER_STR = "hidden_user"
        const val CHAT_STR = "chat"
        const val CHANNEL_STR = "channel"
    }
}
