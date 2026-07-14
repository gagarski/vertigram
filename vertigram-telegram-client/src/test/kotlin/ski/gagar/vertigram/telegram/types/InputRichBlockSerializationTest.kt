package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.BaseSerializationTest.Companion.Mappers
import ski.gagar.vertigram.telegram.types.attachments.StringAttachment
import ski.gagar.vertigram.telegram.types.richmessage.Caption
import ski.gagar.vertigram.telegram.types.richmessage.RichTextValue
import ski.gagar.vertigram.telegram.types.richmessage.TableCell

object InputRichBlockSerializationTest : BaseSerializationTest() {
    private val MAPPERS_TO_SKIP = setOf(Mappers.TELEGRAM)
    private val text = RichTextValue.plain("text")
    private val paragraph = InputRichBlock.Paragraph.create(text = text)
    private val caption = Caption(text = text)
    private val attachment = StringAttachment("https://example.com/media")

    @Test
    fun `input rich block should survive polymorphic serialization`() {
        val blocks = listOf<InputRichBlock>(
            paragraph,
            InputRichBlock.SectionHeading.create(text = text, size = 2),
            InputRichBlock.Preformatted.create(text = text, language = "kotlin"),
            InputRichBlock.Footer.create(text = text),
            InputRichBlock.Divider,
            InputRichBlock.MathematicalExpression.create(expression = "x^2"),
            InputRichBlock.Anchor.create(name = "anchor"),
            InputRichBlock.List.create(
                items = listOf(
                    InputRichBlockListItem.create(
                        blocks = listOf(paragraph),
                        hasCheckbox = true,
                        isChecked = true
                    )
                )
            ),
            InputRichBlock.BlockQuotation.create(blocks = listOf(paragraph), credit = text),
            InputRichBlock.PullQuotation.create(text = text, credit = text),
            InputRichBlock.Collage.create(blocks = listOf(paragraph), caption = caption),
            InputRichBlock.Slideshow.create(blocks = listOf(paragraph), caption = caption),
            InputRichBlock.Table.create(
                cells = listOf(listOf(TableCell(text = text))),
                isBordered = true,
                isStriped = true,
                caption = text
            ),
            InputRichBlock.Details.create(
                summary = text,
                blocks = listOf(paragraph),
                isOpen = true
            ),
            InputRichBlock.Map.create(
                location = Location.create(latitude = 1.0, longitude = 2.0),
                zoom = 10,
                width = 320,
                height = 240,
                caption = caption
            ),
            InputRichBlock.Animation.create(
                animation = InputMedia.Animation.create(media = attachment),
                caption = caption
            ),
            InputRichBlock.Audio.create(
                audio = InputMedia.Audio.create(media = attachment),
                caption = caption
            ),
            InputRichBlock.Photo.create(
                photo = InputMedia.Photo.create(media = attachment),
                caption = caption
            ),
            InputRichBlock.Video.create(
                video = InputMedia.Video.create(media = attachment),
                caption = caption
            ),
            InputRichBlock.VoiceNote.create(
                voiceNote = InputMedia.VoiceNote.create(media = attachment),
                caption = caption
            ),
            InputRichBlock.Thinking.create(text = text)
        )

        blocks.forEach {
            assertSerializable<InputRichBlock>(it, skip = MAPPERS_TO_SKIP)
        }
    }
}
