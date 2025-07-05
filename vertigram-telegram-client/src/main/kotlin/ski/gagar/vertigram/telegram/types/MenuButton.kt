package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [MenuButton](https://core.telegram.org/bots/api#menubutton) type.
 *
 * Subtypes (which are nested) represent the subtypes, described by Telegram docs with more
 * names given they are nested into [MenuButton] class. The rule here is the following:
 * `MenuButtonXxx` Telegram type becomes `MenuButton.Xxx`.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
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
     * Telegram [MenuButtonCommands](https://core.telegram.org/bots/api#menubuttoncommands) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    data object Commands : MenuButton {
        override val type: Type = Type.COMMANDS
    }

    /**
     * Telegram [MenuButtonDefault](https://core.telegram.org/bots/api#menubuttondefault) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    data object Default : MenuButton {
        override val type: Type = Type.DEFAULT
    }

    /**
     * Telegram [MenuButtonWebApp](https://core.telegram.org/bots/api#menubuttonwebapp) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type(wrapRichText = false)
    data class WebApp internal constructor(
        val text: String,
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
