package ski.gagar.vertigram.web.multipart

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

object MultipartContentDispositionTest {
    @Test
    fun `field name should be quoted and escaped`() {
        assertEquals(
            """form-data; name="media \"field\"\\name"""",
            FieldPart("""media "field"\name""", "value").contentDisposition
        )
    }

    @Test
    fun `filename should be quoted and escaped without losing unicode`() {
        assertEquals(
            "form-data; name=\"media\"; filename=\"\u043A\u043E\u0442 \\\"photo\\\"\\\\one.png\"",
            formDataContentDisposition(
                "media",
                "\u043A\u043E\u0442 \"photo\"\\one.png"
            )
        )
    }

    @Test
    fun `control characters should be rejected in field name`() {
        assertThrows(IllegalArgumentException::class.java) {
            FieldPart("media\r\nInjected: value", "value")
        }
    }

    @Test
    fun `control characters should be rejected in filename`() {
        assertThrows(IllegalArgumentException::class.java) {
            formDataContentDisposition("media", "photo.png\r\nInjected: value")
        }
    }
}
