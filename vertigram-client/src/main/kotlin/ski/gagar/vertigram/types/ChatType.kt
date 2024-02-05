package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class ChatType(val group: Boolean) {
    @JsonProperty("private")
    PRIVATE(false),
    @JsonProperty("group")
    GROUP(true),
    @JsonProperty("supergroup")
    SUPERGROUP(true),
    @JsonProperty("channel")
    CHANNEL(false)
}
