package ski.gagar.vertigram.telegram.types

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
            MenuButton.WebApp.create(
                text = "xxx",
                webApp = WebAppInfo.create(
                    url = "https://example.com"
                )
            )
        )

    }

}