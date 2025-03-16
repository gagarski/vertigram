package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object MessageEntitySerializationTest : BaseSerializationTest() {
    @Test
    fun `message entity should survive serialization`() {
        assertSerializable<MessageEntity>(
            MessageEntity.Mention(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Hashtag(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Cashtag(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.BotCommand(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Url(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Email(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.PhoneNumber(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Bold(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Italic(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Underline(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Strikethrough(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Spoiler(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Code(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.Pre(
                offset = 1,
                length = 1
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.TextLink(
                offset = 1,
                length = 1,
                url = "https://"
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.TextMention(
                offset = 1,
                length = 1,
                user = User(id = 1)
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.CustomEmoji(
                offset = 1,
                length = 1,
                customEmojiId = "1"
            )
        )
        assertSerializable<MessageEntity>(
            MessageEntity.BlockQuote(
                offset = 1,
                length = 1
            )
        )

        assertSerializable<MessageEntity>(
            MessageEntity.ExpandableBlockQuote(
                offset = 1,
                length = 1
            )
        )
    }
}