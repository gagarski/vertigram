package ski.gagar.vertigram.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.AnnotatedMember
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.Instant

@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class TgIgnore

@Target(
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class TgEnumName(val name: String)

class UnixTimestampSerializer : JsonSerializer<Instant>() {
    override fun serialize(instant: Instant, gen: JsonGenerator, sp: SerializerProvider?) {
        gen.writeNumber(instant.toEpochMilli() / 1000)
    }

    override fun handledType(): Class<Instant> = Instant::class.java
}

class UnixTimestampDeserializer : JsonDeserializer<Instant>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): Instant {
        return Instant.ofEpochSecond(parser.longValue)
    }

    override fun handledType(): Class<Instant> = Instant::class.java
}

class NoTypeInfoAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun _hasAnnotation(annotated: Annotated, annoClass: Class<out Annotation>?): Boolean {
        if (annoClass == JsonTypeInfo::class.java)
            return false

        return super._hasAnnotation(annotated, annoClass)
    }

    override fun <A : Annotation> _findAnnotation(
        annotated: Annotated,
        annoClass: Class<A>
    ): A? {
        if (annoClass == JsonTypeInfo::class.java)
            return null

        return super._findAnnotation(annotated, annoClass)
    }

}

class TelegramAnnotationIntrospector : NopAnnotationIntrospector() {
    override fun findEnumValues(
        enumType: Class<*>?,
        enumValues: Array<out Enum<*>>,
        names: Array<String>
    ): Array<String> {
        val explicit = mutableMapOf<String, String>()
        enumType ?: return arrayOf()
        for (f in enumType.declaredFields) {
            if (!f.isEnumConstant) {
                continue
            }
            val prop = f.getAnnotation(TgEnumName::class.java) ?: continue
            val n = prop.name
            if (n.isEmpty()) {
                continue
            }
            explicit[f.name] = n
        }
        // and then stitch them together if and as necessary
        for (i in enumValues.indices) {
            val defName = enumValues[i].name
            val explicitValue: String = explicit[defName] ?: continue
            names[i] = explicitValue
        }
        return names
    }

    override fun hasIgnoreMarker(m: AnnotatedMember): Boolean {
        if (m.hasAnnotation(TgIgnore::class.java))
            return true

        return super.hasIgnoreMarker(m)
    }
}

object TelegramModule : Module() {
    override fun getModuleName(): String = "TelegramModule"

    override fun version(): Version = Version.unknownVersion()

    override fun setupModule(context: SetupContext) {
        context.appendAnnotationIntrospector(TelegramAnnotationIntrospector())
    }

}

object UnixTimestampModule : Module() {
    override fun getModuleName(): String = "UnixTimestampModule"

    override fun version(): Version = Version.unknownVersion()

    override fun setupModule(context: SetupContext) {
        context.addSerializers(SimpleSerializers().apply {
            addSerializer(UnixTimestampSerializer())
        })
        context.addDeserializers(SimpleDeserializers().apply {
            addDeserializer(Instant::class.java,
                UnixTimestampDeserializer()
            )
        })
    }
}

val TELEGRAM_JSON_MAPPER: ObjectMapper =
    ObjectMapper() // default mapper
        .setAnnotationIntrospector(NoTypeInfoAnnotationIntrospector())
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule()) // Kotlin!
        .registerModule(TelegramModule)
        .registerModule(UnixTimestampModule)
