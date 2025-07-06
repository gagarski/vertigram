package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.util.toChatId

object BotCommandSerializationTest : BaseSerializationTest() {
    @Test
    fun `bot command scope should survive serialization`() {
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.Default
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.AllPrivateChats
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.AllGroupChats
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.AllChatAdministrators
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.Chat.create(
                chatId = 1.toChatId()
            )
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.ChatAdministrators.create(
                chatId = 1.toChatId()
            )
        )
        assertSerializable<BotCommand.Scope>(
            BotCommand.Scope.ChatMember.create(
                chatId = 1.toChatId(),
                userId = 1
            )
        )

    }

}