package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object MenuButtonSerializationTest : BaseSerializationTest() {
    @Test
    fun `menu buttons should survive serialization`() {
        assertSerializable<MenuButton>(
            MenuButton.Default
        )
        assertSerializable<MenuButton>(
            MenuButton.Commands
        )
        assertSerializable<MenuButton>(
            MenuButton.WebApp(
                text = "xxx",
                webApp = WebAppInfo(
                    url = "https://example.com"
                )
            )
        )

    }

}