package ski.gagar.vertigram.telegram.types.richmessage

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.User
import java.time.Instant

object RichMessageSerializationTest : BaseSerializationTest() {
    @Test
    fun `rich text should survive serialization`() {
        assertSerializable<RichText>(
            RichText.Bold(text = RichTextValue.plain("bold"))
        )
        assertSerializable<RichText>(
            RichText.DateTime(
                text = RichTextValue.plain("date"),
                unixTime = Instant.ofEpochSecond(1),
                dateTimeFormat = "dd.MM.yyyy"
            )
        )
        assertSerializable<RichText>(
            RichText.TextMention(
                text = RichTextValue.plain("user"),
                user = User(id = 1, firstName = "Test")
            )
        )
        assertSerializable<RichText>(
            RichText.BotCommandText(
                text = RichTextValue.plain("/start"),
                botCommand = "/start"
            )
        )
        assertSerializable<RichText>(
            RichText.ReferenceLink(
                text = RichTextValue.plain("reference"),
                referenceName = "docs"
            )
        )
        assertSerializable<RichText>(
            RichText.Italic(
                text = RichTextValue.parts(
                    listOf(
                        RichTextValue.plain("hello "),
                        RichTextValue.formatted(RichText.Bold(RichTextValue.plain("world")))
                    )
                )
            )
        )
    }

    @Test
    fun `rich blocks should survive serialization`() {
        assertSerializable<Block>(
            Block.Paragraph(text = RichTextValue.plain("paragraph"))
        )
        assertSerializable<Block>(
            Block.SectionHeading(text = RichTextValue.plain("heading"), level = 2)
        )
        assertSerializable<Block>(
            Block.Divider
        )
        assertSerializable<Block>(
            Block.List(
                items = listOf(
                    ListItem(
                        blocks = listOf(Block.Paragraph(text = RichTextValue.plain("item"))),
                        checked = true
                    )
                )
            )
        )
        assertSerializable<Block>(
            Block.Details(
                summary = RichTextValue.plain("More"),
                blocks = listOf(Block.Paragraph(text = RichTextValue.plain("details"))),
                isOpen = true
            )
        )
        assertSerializable<Block>(
            Block.Table(
                cells = listOf(
                    listOf(TableCell(text = RichTextValue.plain("head"), isHeader = true))
                ),
                caption = RichTextValue.plain("caption")
            )
        )
    }

    @Test
    fun `rich message should survive serialization`() {
        assertSerializable<RichMessage>(
            RichMessage(
                blocks = listOf(
                    Block.Paragraph(text = RichTextValue.plain("body"))
                ),
                isRtl = true
            )
        )
    }
}
