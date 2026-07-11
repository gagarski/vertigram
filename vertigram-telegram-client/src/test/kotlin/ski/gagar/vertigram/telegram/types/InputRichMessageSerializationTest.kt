package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

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
    }
}
