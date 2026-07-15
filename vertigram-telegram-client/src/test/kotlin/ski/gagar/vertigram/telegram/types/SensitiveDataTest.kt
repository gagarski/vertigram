package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.types.methods.AnswerInlineQuery
import ski.gagar.vertigram.telegram.types.methods.CreateInvoiceLink
import ski.gagar.vertigram.telegram.types.methods.GetManagedBotToken

class SensitiveDataTest {
    @Test
    fun `creates a redacted request copy without changing original`() {
        val request = CreateInvoiceLink(
            title = "title",
            description = "description",
            payload = "payload",
            providerToken = "provider-secret",
            currency = "USD",
            prices = listOf(LabeledPrice("price", 100)),
            isFlexible = false
        )

        val redacted = request.copyWithoutSensitiveData()

        assertNotSame(request, redacted)
        assertEquals("provider-secret", request.providerToken)
        assertEquals(REDACTED_SENSITIVE_DATA, redacted.providerToken)
    }

    @Test
    fun `redacts invoice content nested in inline query result copy`() {
        val invoice = InlineQuery.InputMessageContent.Invoice(
            title = "title",
            description = "description",
            payload = "payload",
            providerToken = "provider-secret",
            currency = "USD",
            prices = listOf(LabeledPrice("price", 100))
        )
        val request = AnswerInlineQuery(
            inlineQueryId = "query",
            results = listOf(InlineQuery.Result.Article("result", "title", invoice))
        )

        val redacted = request.copyWithoutSensitiveData()
        val redactedInvoice = (redacted.results.single() as InlineQuery.Result.Article)
            .inputMessageContent as InlineQuery.InputMessageContent.Invoice

        assertEquals("provider-secret", invoice.providerToken)
        assertEquals(REDACTED_SENSITIVE_DATA, redactedInvoice.providerToken)
    }

    @Test
    fun `redacts sensitive method result`() {
        val call = GetManagedBotToken(userId = 1)

        assertEquals(REDACTED_SENSITIVE_DATA, "managed-bot-token".withoutSensitiveResult(call))
    }
}
