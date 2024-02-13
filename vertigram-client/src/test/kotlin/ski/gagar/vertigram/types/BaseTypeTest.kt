package ski.gagar.vertigram.types

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import org.junit.jupiter.api.Assertions
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER



abstract class BaseTypeTest {
    fun assertSerializable(obj: Any, type: Class<*>, mapper: ObjectMapper, mapperName: String = "unknown") {
        Assertions.assertEquals(obj, mapper.readValue(mapper.writeValueAsString(obj), type),
            "Serialization check failed for mapper $mapperName")
    }

    fun assertSerializable(obj: Any, type: Class<*>) {
        for ((mapperName, mapper) in MAPPERS) {
            assertSerializable(obj, type, mapper, mapperName)
        }
    }

    inline fun <reified T> assertSerializable(obj: Any) {
        assertSerializable(obj, T::class.java)
    }

    companion object {
        val MAPPERS = mapOf(
            "telegram" to TELEGRAM_JSON_MAPPER,
            "vanilla with modules" to ObjectMapper()
                .registerModule(KotlinModule.Builder().build())
                .registerModule(JavaTimeModule()),
            "vertx with modules" to DatabindCodec.mapper()
                .registerModule(KotlinModule.Builder().build())
                .registerModule(JavaTimeModule())
        )
    }
}