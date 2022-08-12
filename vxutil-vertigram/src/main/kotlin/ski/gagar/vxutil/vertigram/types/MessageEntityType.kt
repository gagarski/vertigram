package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class MessageEntityType {
    @JsonProperty("mention")
    MENTION,
    @JsonProperty("hashtag")
    HASHTAG,
    @JsonProperty("cashtag")
    CASHTAG,
    @JsonProperty("bot_command")
    BOT_COMMAND,
    @JsonProperty("url")
    URL,
    @JsonProperty("email")
    EMAIL,
    @JsonProperty("phone_number")
    PHONE_NUMBER,
    @JsonProperty("bold")
    BOLD,
    @JsonProperty("italic")
    ITALIC,
    @JsonProperty("underline")
    UNDERLINE,
    @JsonProperty("strikethrough")
    STRIKETHROUGH,
    @JsonProperty("spoiler")
    SPOILER,
    @JsonProperty("code")
    CODE,
    @JsonProperty("pre")
    PRE,
    @JsonProperty("text_link")
    TEXT_LINK,
    @JsonProperty("text_mention")
    TEXT_MENTION,
    @JsonProperty("custom_emoji")
    CUSTOM_EMOJI
}
