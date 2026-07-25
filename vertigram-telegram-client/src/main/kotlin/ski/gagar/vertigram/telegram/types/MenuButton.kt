package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * This object describes the bot's menu button in a private chat.
 *
 * See Telegram's [MenuButton](https://core.telegram.org/bots/api#menubutton) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = MenuButton.Commands::class, name = MenuButton.Type.COMMANDS_STR),
    JsonSubTypes.Type(value = MenuButton.Default::class, name = MenuButton.Type.DEFAULT_STR),
    JsonSubTypes.Type(value = MenuButton.WebApp::class, name = MenuButton.Type.WEB_APP_STR),
)
sealed interface MenuButton {
    val type: Type

    /**
     * Represents a menu button which opens the bot's list of commands.
     *
     * See Telegram's [MenuButtonCommands](https://core.telegram.org/bots/api#menubuttoncommands) documentation.
     */
    data object Commands : MenuButton {
        override val type: Type = Type.COMMANDS
    }

    /**
     * Describes that no specific value for the menu button was set.
     *
     * See Telegram's [MenuButtonDefault](https://core.telegram.org/bots/api#menubuttondefault) documentation.
     */
    data object Default : MenuButton {
        override val type: Type = Type.DEFAULT
    }

    /**
     * Represents a menu button which launches a Web App.
     *
     * See Telegram's [MenuButtonWebApp](https://core.telegram.org/bots/api#menubuttonwebapp) documentation.
     */
    @TelegramCodegen.Type
    data class WebApp internal constructor(
        /** Text on the button. */
        val text: String,
        /** Description of the Web App that will be launched when the user presses the button. */
        val webApp: WebAppInfo
    ) : MenuButton {
        override val type: Type = Type.WEB_APP

        companion object
    }

    /**
     * Value for [type].
     */
    enum class Type {
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

}
