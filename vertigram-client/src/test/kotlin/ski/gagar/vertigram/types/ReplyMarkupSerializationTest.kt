package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test

object ReplyMarkupSerializationTest : BaseSerializationTest() {
    @Test
    fun `reply markup types should survive serialization`() {
        assertSerializable<ReplyMarkup>(
            ReplyMarkup.InlineKeyboard(
                inlineKeyboard = listOf(listOf())
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(listOf())
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.KeyboardRemove(
                selective = true
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.ForceReply(
                selective = true
            )
        )
    }

    @Test
    fun `inline keyboard buttons should survive serialization`() {
        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Text(
                text = "Text"
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Url(
                text = "Text",
                url = "https://example.com"
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Callback(
                text = "Text",
                callbackData = "bla"
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.WebApp(
                text = "Text",
                webApp = WebAppInfo(url = "https://example.com")
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Login(
                text = "Text",
                loginUrl = ReplyMarkup.InlineKeyboard.Button.Login.Payload(
                    url = "https://example.com"
                )
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.SwitchInline(
                text = "Text",
                switchInlineQuery = "bla"
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.SwitchInlineCurrentChat(
                text = "Text",
                switchInlineQueryCurrentChat = "bla"
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.SwitchInlineChosenChat(
                text = "Text",
                switchInlineQueryChosenChat = ReplyMarkup.InlineKeyboard.Button.SwitchInlineChosenChat.Payload()
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Game(
                text = "Text",
                callbackGame = ReplyMarkup.InlineKeyboard.Button.Game.Payload
            )
        )

        assertSerializable<ReplyMarkup.InlineKeyboard.Button>(
            ReplyMarkup.InlineKeyboard.Button.Pay(
                text = "Text"
            )
        )
    }


    @Test
    fun `keyboard buttons should survive serialization`() {
        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.Text(
                text = "Text"
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.RequestUsers(
                text = "Text",
                requestUsers = ReplyMarkup.Keyboard.Button.RequestUsers.Payload(
                    requestId = 1
                )
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.RequestChat(
                text = "Text",
                requestChat = ReplyMarkup.Keyboard.Button.RequestChat.Payload(
                    requestId = 1,
                    chatIsChannel = true
                )
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.RequestContact(
                text = "Text"
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.RequestLocation(
                text = "Text"
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.RequestPoll(
                text = "Text",
                requestPoll = ReplyMarkup.Keyboard.Button.RequestPoll.Payload()
            )
        )

        assertSerializable<ReplyMarkup.Keyboard.Button>(
            ReplyMarkup.Keyboard.Button.WebApp(
                text = "Text",
                webApp = WebAppInfo("http://example.com")
            )
        )
    }
}