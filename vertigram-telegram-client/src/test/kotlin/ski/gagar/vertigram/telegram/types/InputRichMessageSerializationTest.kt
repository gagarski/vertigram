package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.BaseSerializationTest.Companion.Mappers
import ski.gagar.vertigram.telegram.types.attachments.StringAttachment
import ski.gagar.vertigram.telegram.types.richmessage.RichTextValue

object InputRichMessageSerializationTest : BaseSerializationTest() {
    @Test
    fun `input rich message should survive serialization`() {
        assertSerializable<InputRichMessage>(
            InputRichMessage.Html(
                html = "<p>Hello</p>",
                isRtl = true,
                skipEntityDetection = true
            )
        )
        assertSerializable<InputRichMessage>(
            InputRichMessage.Markdown(
                markdown = "# Hello",
                isRtl = true,
                skipEntityDetection = true
            )
        )
        assertSerializable<InputRichMessage>(
            InputRichMessage.Blocks.create(
                blocks = listOf(
                    InputRichBlock.Paragraph.create(text = RichTextValue.plain("Hello"))
                ),
                isRtl = true,
                skipEntityDetection = true
            )
        )
        assertSerializable<InputRichMessage>(
            InputRichMessage.Html.create(
                html = "<img src=\"tg://photo?id=photo\">",
                media = listOf(
                    InputRichMessageMedia.create(
                        id = "photo",
                        media = InputMedia.Photo.create(
                            media = StringAttachment("https://example.com/photo")
                        )
                    )
                )
            ),
            skip = setOf(Mappers.TELEGRAM)
        )
    }
}
