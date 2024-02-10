package ski.gagar.vertigram.types

data class MenuButtonWebApp(
    val text: String,
    val webApp: WebAppInfo
) : MenuButton {
    override val type: MenuButtonType = MenuButtonType.WEB_APP
}