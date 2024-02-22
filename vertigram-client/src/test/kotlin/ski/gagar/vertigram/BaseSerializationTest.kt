package ski.gagar.vertigram

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import org.junit.jupiter.api.Assertions
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER


/**
 * A base class for tests that preform checks for serializability/deserializability of objects.
 *
 * It's recommended to have such cases for all types which are supposed to be deserialized using supertypes
 */
abstract class BaseSerializationTest {
    fun assertSerializable(obj: Any, type: Class<*>, mapper: ObjectMapper, mapperName: String = "unknown") {
        Assertions.assertEquals(obj, mapper.readValue(mapper.writeValueAsString(obj), type),
            "Serialization check failed for mapper $mapperName")
    }

    fun assertSerializable(obj: Any, type: Class<*>, skip: Set<String>) {
        for ((mapperName, mapper) in MAPPERS) {
            if (mapperName in skip)
                continue
            assertSerializable(obj, type, mapper, mapperName)
        }
    }

    inline fun <reified T> assertSerializable(obj: Any, skip: Set<String> = setOf()) {
        assertSerializable(obj, T::class.java, skip)
    }

    companion object {
        val MAPPERS = mapOf(
            Mappers.TELEGRAM to TELEGRAM_JSON_MAPPER,
            Mappers.VANILLA_WITH_MODULES to ObjectMapper()
                .registerModule(KotlinModule.Builder().build())
                .registerModule(JavaTimeModule()),
            Mappers.VERTX_WITH_MODULES to DatabindCodec.mapper()
                .registerModule(KotlinModule.Builder().build())
                .registerModule(JavaTimeModule())
        )
        object Mappers {
            const val TELEGRAM = "telegram"
            const val VANILLA_WITH_MODULES = "vanilla with modules"
            const val VERTX_WITH_MODULES = "vanilla with modules"
        }
    }
}