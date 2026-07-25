package ski.gagar.vertigram.util

import com.fasterxml.jackson.core.type.TypeReference
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.types.methods.AddStickerToSet
import ski.gagar.vertigram.telegram.types.methods.AnswerPreCheckoutQuery
import ski.gagar.vertigram.telegram.types.methods.GetUpdatesRaw
import ski.gagar.vertigram.telegram.types.methods.JsonTelegramCallable
import ski.gagar.vertigram.telegram.types.methods.SendPhoto
import ski.gagar.vertigram.telegram.types.methods.TelegramCallable
import java.lang.reflect.Modifier

@Suppress("DEPRECATION")
class VertigramTypeHintsTest {
    @Test
    fun `generated descriptors contain only concrete Telegram methods`() {
        val descriptor = VertigramTypeHints.descriptorByCallable.getOrAssert(GetUpdatesRaw::class.java)

        assertEquals(GetUpdatesRaw::class.java, descriptor.callableClass)
        assertEquals("getUpdates", descriptor.telegramMethodName)
        assertFalse(TelegramCallable::class.java in VertigramTypeHints.descriptorByCallable)
        assertFalse(JsonTelegramCallable::class.java in VertigramTypeHints.descriptorByCallable)
        assertTrue(
            VertigramTypeHints.descriptorByCallable.keys.all {
                !Modifier.isAbstract(it.modifiers)
            }
        )
        assertTrue(
            VertigramTypeHints.descriptorByCallable.all { (callable, value) ->
                callable == value.callableClass
            }
        )
        assertEquals(
            VertigramTypeHints.descriptorByCallable.size,
            VertigramTypeHints.descriptorByTgvAddress.size
        )
    }

    @Test
    fun `generated response types preserve generic arguments`() {
        val expected = TELEGRAM_TYPE_FACTORY.constructType(
            object : TypeReference<List<Map<String, Any?>>>() {}.type
        )

        assertEquals(
            expected,
            VertigramTypeHints.descriptorByCallable.getOrAssert(GetUpdatesRaw::class.java).responseType
        )
        assertEquals(
            TELEGRAM_TYPE_FACTORY.constructType(Boolean::class.javaObjectType),
            VertigramTypeHints.descriptorByCallable
                .getOrAssert(AnswerPreCheckoutQuery.Ok::class.java)
                .responseType
        )
    }

    @Test
    fun `metadata-only methods retain request type and consumer settings`() {
        val descriptor = VertigramTypeHints.descriptorByCallable.getOrAssert(GetUpdatesRaw::class.java)

        assertFalse(descriptor.generateVerticleConsumer)
        assertEquals(TelegramCallableTransport.JSON, descriptor.transport)
        assertEquals(
            TELEGRAM_TYPE_FACTORY.constructType(GetUpdatesRaw::class.java),
            descriptor.requestType
        )
        assertEquals(descriptor, VertigramTypeHints.descriptorByTgvAddress[descriptor.tgvAddress])
        assertEquals(
            TelegramCallableTransport.MULTIPART,
            VertigramTypeHints.descriptorByCallable.getOrAssert(SendPhoto::class.java).transport
        )
    }

    @Test
    fun `callable addresses use simple names and preserve nesting`() {
        assertEquals(
            "addStickerToSet",
            VertigramTypeHints.descriptorByCallable
                .getOrAssert(AddStickerToSet::class.java)
                .tgvAddress
        )
        assertEquals(
            "answerPreCheckoutQuery.ok",
            VertigramTypeHints.descriptorByCallable
                .getOrAssert(AnswerPreCheckoutQuery.Ok::class.java)
                .tgvAddress
        )
    }
}
