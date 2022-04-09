package ski.gagar.vxutil.vertigram.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.AnnotatedMember
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import com.fasterxml.jackson.module.kotlin.KotlinModule
import ski.gagar.vxutil.uncheckedCast
import ski.gagar.vxutil.uncheckedCastOrNull
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import ski.gagar.vxutil.vertigram.types.attachments.UrlAttachment
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.LongChatId
import ski.gagar.vxutil.vertigram.types.StringChatId
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
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FIELD
)
@Retention(AnnotationRetention.RUNTIME)
annotation class TgIgnoreTypeInfo


class ChatIdSerializer : JsonSerializer<ChatId>() {
    override fun serialize(value: ChatId, gen: JsonGenerator, serializers: SerializerProvider) = when(value) {
        is LongChatId -> {
            gen.writeNumber(value.long)
        }
        is StringChatId -> {
            gen.writeString(value.string)
        }
    }

    override fun serializeWithType(
        value: ChatId,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) {
        serialize(value, gen, serializers)
    }

    override fun handledType(): Class<ChatId> = ChatId::class.java
}

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

class ChatIdDeserializer : JsonDeserializer<ChatId>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext?): ChatId = when (parser.currentToken) {
        JsonToken.VALUE_NUMBER_INT -> LongChatId(parser.longValue)
        JsonToken.VALUE_STRING -> StringChatId(parser.valueAsString)

        else -> {
            throw JsonMappingException.from(parser, "chatId should be either integer or string")
        }
    }

    override fun handledType(): Class<ChatId> = ChatId::class.java
}

class AttachmentSerializer : JsonSerializer<Attachment>() {
    override fun serialize(value: Attachment, gen: JsonGenerator, serializers: SerializerProvider) =
        throw JsonMappingException.from(gen, "Attachment is not serializable, please serialize UrlAttachment")

    override fun handledType(): Class<Attachment> = Attachment::class.java
}

class AttachmentDeserializer : JsonDeserializer<Attachment>() {
    override fun deserialize(parser: JsonParser, ctxt: DeserializationContext): Attachment =
        when (parser.currentToken) {
            JsonToken.VALUE_STRING -> UrlAttachment(parser.valueAsString)
            else -> {
                throw JsonMappingException.from(parser, "attachment should be string")
            }
        }

    override fun handledType(): Class<Attachment> = Attachment::class.java
}

class UrlAttachmentSerializer : JsonSerializer<UrlAttachment>() {
    override fun serialize(value: UrlAttachment, gen: JsonGenerator, serializers: SerializerProvider) =
        gen.writeString(value.url)

    override fun serializeWithType(
        value: UrlAttachment,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) = serialize(value, gen, serializers)

    override fun handledType(): Class<UrlAttachment> = UrlAttachment::class.java
}

class NoTypeInfoAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun _hasAnnotation(annotated: Annotated, annoClass: Class<out Annotation>?): Boolean {
        if (annoClass != JsonTypeInfo::class.java)
            return super._hasAnnotation(annotated, annoClass)

        return !_hasAnnotation(annotated, TgIgnoreTypeInfo::class.java)
    }

    override fun <A : Annotation> _findAnnotation(
        annotated: Annotated,
        annoClass: Class<A>
    ): A? {
        if (annoClass != JsonTypeInfo::class.java)
            return super._findAnnotation(annotated, annoClass)

        if (_findAnnotation(annotated, TgIgnoreTypeInfo::class.java) != null) {
            return null
        }

        return super._findAnnotation(annotated, annoClass.uncheckedCast<Class<A>>())
    }

}

class TelegramAnnotationIntrospector : NopAnnotationIntrospector() {
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

        context.addSerializers(SimpleSerializers().apply {
            addSerializer(UnixTimestampSerializer())
            addSerializer(ChatIdSerializer())
            addSerializer(AttachmentSerializer())
            addSerializer(UrlAttachmentSerializer())
        })
        context.addDeserializers(SimpleDeserializers().apply {
            addDeserializer(Instant::class.java,
                UnixTimestampDeserializer()
            )
            addDeserializer(ChatId::class.java,
                ChatIdDeserializer()
            )
            addDeserializer(
                Attachment::class.java,
                AttachmentDeserializer()
            )
        })
    }

}

val TELEGRAM_JSON_MAPPER: ObjectMapper =
    ObjectMapper() // default mapper
        .setAnnotationIntrospector(NoTypeInfoAnnotationIntrospector())
        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule())
        .registerModule(TelegramModule)
