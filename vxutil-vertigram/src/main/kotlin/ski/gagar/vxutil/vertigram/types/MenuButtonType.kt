package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class MenuButtonType {
    @JsonProperty(COMMANDS_STR)
    COMMANDS,
    @JsonProperty(WEB_APP_STR)
    WEB_APP,
    @JsonProperty(DEFAULT_STR)
    DEFAULT;
    companion object {
        const val COMMANDS_STR = "commands"
        const val WEB_APP_STR = "web_app"
        const val DEFAULT_STR = "default"
    }
}
