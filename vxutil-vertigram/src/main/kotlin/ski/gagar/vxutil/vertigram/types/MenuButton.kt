package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = MenuButtonCommands::class, name = MenuButtonType.COMMANDS_STR),
    JsonSubTypes.Type(value = MenuButtonWebApp::class, name = MenuButtonType.WEB_APP_STR),
    JsonSubTypes.Type(value = MenuButtonDefault::class, name = MenuButtonType.DEFAULT_STR),
)
sealed interface MenuButton {
    val type: MenuButtonType
}
