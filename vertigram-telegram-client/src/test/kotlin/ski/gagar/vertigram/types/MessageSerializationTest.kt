package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import java.time.Instant
import java.time.temporal.ChronoUnit

object MessageSerializationTest : BaseSerializationTest() {
    @Test
    fun `message origin should survive serialization`() {
        assertSerializable<Message.Origin>(
            Message.Origin.Chat.create(
                date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                senderChat = Chat.create(
                    id = 1,
                    type = Chat.Type.PRIVATE
                )
            )
        )
        assertSerializable<Message.Origin>(
            Message.Origin.User.create(
                date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                senderUser = User.create(
                    id = 1
                )
            )
        )
        assertSerializable<Message.Origin>(
            Message.Origin.HiddenUser.create(
                date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                senderUserName = "Alex"
            )
        )
        assertSerializable<Message.Origin>(
            Message.Origin.Channel.create(
                date = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                messageId = 1,
                chat = Chat.create(
                    id = 1,
                    type = Chat.Type.CHANNEL
                )
            )
        )
    }

    @Test
    fun `service message WriteAccessAllowed should survive serialization`() {
        assertSerializable<Message.Service.WriteAccessAllowed>(
            Message.Service.WriteAccessAllowed.FromRequest.create()
        )
        assertSerializable<Message.Service.WriteAccessAllowed>(
            Message.Service.WriteAccessAllowed.WebApp.create(
                webAppName = "111"
            )
        )
        assertSerializable<Message.Service.WriteAccessAllowed>(
            Message.Service.WriteAccessAllowed.FromAttachmentMenu.create()
        )
    }

}