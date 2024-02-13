package ski.gagar.vertigram.types

import org.junit.jupiter.api.Test

object ReplyMarkupSerializationTest : BaseTypeTest() {
    @Test
    fun `inline keyboard should survive serialization`() {
        assertSerializable<ReplyMarkup>(
            ReplyMarkup.InlineKeyboard(
                inlineKeyboard = listOf(listOf())
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.InlineKeyboard(
                inlineKeyboard = listOf(
                    listOf(
                        ReplyMarkup.InlineKeyboard.Button(text = "Test")
                    )
                )
            )
        )
    }

    @Test
    fun `keyboard should survive serialization`() {
        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(listOf())
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(
                    listOf(
                        ReplyMarkup.Keyboard.Button.Text(text = "test")
                    )
                )
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(
                    listOf(
                        ReplyMarkup.Keyboard.Button.Text(text = "test")
                    )
                ),
                resizeKeyboard = true
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(
                    listOf(
                        ReplyMarkup.Keyboard.Button.Text(text = "test")
                    )
                ),
                selective = true
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.Keyboard(
                keyboard = listOf(
                    listOf(
                        ReplyMarkup.Keyboard.Button.Text(text = "test")
                    )
                ),
                selective = true,
                isPersistent = true
            )
        )
    }

    @Test
    fun `keyboard remove should survive serialization`() {
        assertSerializable<ReplyMarkup>(
            ReplyMarkup.KeyboardRemove(
                selective = true
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.KeyboardRemove(
                selective = false
            )
        )
    }

    @Test
    fun `force reply should survive serialization`() {
        assertSerializable<ReplyMarkup>(
            ReplyMarkup.ForceReply(
                selective = true
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.ForceReply(
                selective = false
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.ForceReply(
                selective = false,
                inputFieldPlaceholder = "xxx"
            )
        )

        assertSerializable<ReplyMarkup>(
            ReplyMarkup.ForceReply(
                selective = true,
                inputFieldPlaceholder = "xxx"
            )
        )
    }

}