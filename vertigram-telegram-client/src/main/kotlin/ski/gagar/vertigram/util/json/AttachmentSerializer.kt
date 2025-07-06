package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import ski.gagar.vertigram.telegram.types.attachments.Attachment
import ski.gagar.vertigram.util.internal.uncheckedCastOrNull
import ski.gagar.vertigram.util.multipart.OutputStreamWrapper
import java.util.*

internal class AttachmentSerializer : JsonSerializer<Attachment>() {
    override fun serialize(value: Attachment, gen: JsonGenerator, serializers: SerializerProvider) {
        val fieldName = getRandomName()
        val reference = value.getReference(fieldName)
        gen.writeString(reference.url)

        val osw = gen.outputTarget.uncheckedCastOrNull<OutputStreamWrapper>() ?: return

        val deferred = value.getReferredPart(fieldName, osw.vertx)
        if (null != deferred)
            osw.defer(fieldName, deferred)
    }

    override fun serializeWithType(
        value: Attachment,
        gen: JsonGenerator,
        serializers: SerializerProvider,
        typeSer: TypeSerializer
    ) {
        serialize(value, gen, serializers)
    }

    fun getRandomName(): String = "deferred_attachment_${UUID.randomUUID()}"

    override fun handledType(): Class<Attachment> = Attachment::class.java

    companion object {
        const val VERTX = "vertx"
        const val DEFERRED = "vertx"
    }
}
