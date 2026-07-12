package ski.gagar.vertigram.telegram.types.richmessage

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.Audio
import ski.gagar.vertigram.telegram.types.Location
import ski.gagar.vertigram.telegram.types.PhotoSize
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.Video
import ski.gagar.vertigram.telegram.types.Voice
import java.time.Instant
import java.time.Duration

object RichMessageSerializationTest : BaseSerializationTest() {
    private val text = RichTextValue.plain("text")

    @Test
    fun `rich text should survive serialization`() {
        assertSerializable<RichText>(RichText.Bold(text = text))
        assertSerializable<RichText>(RichText.Italic(text = text))
        assertSerializable<RichText>(RichText.Underline(text = text))
        assertSerializable<RichText>(RichText.Strikethrough(text = text))
        assertSerializable<RichText>(RichText.Spoiler(text = text))
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
        assertSerializable<RichText>(RichText.Subscript(text = text))
        assertSerializable<RichText>(RichText.Superscript(text = text))
        assertSerializable<RichText>(RichText.Marked(text = text))
        assertSerializable<RichText>(RichText.Code(text = text))
        assertSerializable<RichText>(RichText.CustomEmoji(customEmojiId = "1", alternativeText = "emoji"))
        assertSerializable<RichText>(RichText.MathematicalExpression(expression = "x + y"))
        assertSerializable<RichText>(RichText.Url(text = text, url = "https://example.com"))
        assertSerializable<RichText>(RichText.EmailAddress(text = text, emailAddress = "test@example.com"))
        assertSerializable<RichText>(RichText.PhoneNumber(text = text, phoneNumber = "+10000000000"))
        assertSerializable<RichText>(RichText.BankCardNumber(text = text, bankCardNumber = "4242424242424242"))
        assertSerializable<RichText>(RichText.Mention(text = text, username = "telegram"))
        assertSerializable<RichText>(RichText.Hashtag(text = text, hashtag = "tag"))
        assertSerializable<RichText>(RichText.Cashtag(text = text, cashtag = "TGRM"))
        assertSerializable<RichText>(RichText.BotCommandText(text = RichTextValue.plain("/start"), botCommand = "/start"))
        assertSerializable<RichText>(RichText.Anchor(name = "anchor"))
        assertSerializable<RichText>(RichText.AnchorLink(text = text, anchorName = "anchor"))
        assertSerializable<RichText>(RichText.Reference(name = "docs", text = text))
        assertSerializable<RichText>(RichText.ReferenceLink(text = RichTextValue.plain("reference"), referenceName = "docs"))
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
        val paragraph = Block.Paragraph(text = RichTextValue.plain("paragraph"))
        val caption = Caption(text = RichTextValue.plain("caption"), credit = RichTextValue.plain("credit"))
        val photo = PhotoSize(fileId = "photo", fileUniqueId = "unique-photo", width = 1, height = 1)

        assertSerializable<Block>(paragraph)
        assertSerializable<Block>(Block.SectionHeading(text = RichTextValue.plain("heading"), size = 2))
        assertSerializable<Block>(Block.Preformatted(text = text, language = "kotlin"))
        assertSerializable<Block>(Block.Footer(text = text))
        assertSerializable<Block>(Block.Divider)
        assertSerializable<Block>(Block.MathematicalExpression(expression = "x + y"))
        assertSerializable<Block>(Block.Anchor(name = "anchor"))
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
        assertSerializable<Block>(Block.BlockQuotation(blocks = listOf(paragraph), credit = text))
        assertSerializable<Block>(Block.PullQuotation(text = text, credit = text))
        assertSerializable<Block>(Block.Collage(blocks = listOf(paragraph), caption = caption))
        assertSerializable<Block>(Block.Slideshow(blocks = listOf(paragraph), caption = caption))
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
        assertSerializable<Block>(
            Block.Map(
                location = Location(latitude = 1.0, longitude = 2.0),
                zoom = 1,
                width = 320,
                height = 240,
                caption = caption
            )
        )
        assertSerializable<Block>(
            Block.Animation(
                animation = ski.gagar.vertigram.telegram.types.Animation(
                    fileId = "animation",
                    fileUniqueId = "unique-animation",
                    width = 1,
                    height = 1,
                    duration = Duration.ofSeconds(1)
                ),
                hasSpoiler = true,
                caption = caption
            )
        )
        assertSerializable<Block>(
            Block.Audio(
                audio = Audio(
                    fileId = "audio",
                    fileUniqueId = "unique-audio",
                    duration = Duration.ofSeconds(1)
                ),
                caption = caption
            )
        )
        assertSerializable<Block>(Block.Photo(photo = listOf(photo), hasSpoiler = true, caption = caption))
        assertSerializable<Block>(
            Block.Video(
                video = Video(
                    fileId = "video",
                    fileUniqueId = "unique-video",
                    width = 1,
                    height = 1,
                    duration = Duration.ofSeconds(1)
                ),
                hasSpoiler = true,
                caption = caption
            )
        )
        assertSerializable<Block>(
            Block.VoiceNote(
                voiceNote = Voice(
                    fileId = "voice",
                    fileUniqueId = "unique-voice",
                    duration = Duration.ofSeconds(1)
                ),
                caption = caption
            )
        )
        assertSerializable<Block>(Block.Thinking(text = text))
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
