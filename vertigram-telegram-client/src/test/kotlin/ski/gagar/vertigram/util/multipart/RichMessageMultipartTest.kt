package ski.gagar.vertigram.util.multipart

import io.vertx.core.Vertx
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.markup.richMessageHtml
import ski.gagar.vertigram.telegram.markup.richMessageBlocks
import ski.gagar.vertigram.telegram.types.attachments.AbstractFileAttachment
import ski.gagar.vertigram.telegram.types.methods.SendRichMessage
import ski.gagar.vertigram.telegram.types.util.toChatId
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.web.multipart.FieldPart

object RichMessageMultipartTest {
    @Test
    fun `multipart mapper should extract nested rich message attachments`() {
        val vertx = Vertx.vertx()
        try {
            val mapper = telegramJsonMapperWithMultipart(TELEGRAM_JSON_MAPPER, vertx)
            val requests = listOf(
                SendRichMessage(
                    chatId = 1.toChatId(),
                    richMessage = richMessageHtml {
                        photo(TestAttachment)
                    }
                ),
                SendRichMessage(
                    chatId = 1.toChatId(),
                    richMessage = richMessageBlocks {
                        photo(TestAttachment)
                    }
                )
            )

            requests.forEach { request ->
                val form = mapper.toMultipart(request)
                assertEquals(3, form.parts.size)
                assertTrue(form.parts.any { it.contentDisposition.contains("name=\"chat_id\"") })
                assertTrue(form.parts.any { it.contentDisposition.contains("name=\"rich_message\"") })
                assertTrue(form.parts.any { it.contentDisposition.contains("name=\"deferred_attachment_") })
            }
        } finally {
            vertx.close()
        }
    }

    private data object TestAttachment : AbstractFileAttachment() {
        override fun doAttach(field: String, vertx: Vertx) = FieldPart(field, "contents")
    }
}
