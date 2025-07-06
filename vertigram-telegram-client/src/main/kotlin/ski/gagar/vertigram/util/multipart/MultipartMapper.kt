package ski.gagar.vertigram.util.multipart

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.Vertx
import ski.gagar.vertigram.util.internal.uncheckedCast
import ski.gagar.vertigram.util.json.MultipartModule
import ski.gagar.vertigram.web.multipart.FieldPart
import ski.gagar.vertigram.web.multipart.MultipartForm
import java.io.ByteArrayOutputStream

/**
 * A multipart serializer which piggybacks regular ObjectMapper.
 *
 * Multipart format is only top-level format, while nested fields can be JSON values.
 *
 */
internal class MultipartMapper(jsonMapper: ObjectMapper, private val vertx: Vertx) {
    private val mapper = jsonMapper.copy().registerModule(MultipartModule)

    fun toMultipart(obj: Any): MultipartForm = MultipartForm(buildList {
        val baos = ByteArrayOutputStream()
        // Wrapper can memorize deferred attachments, AttachmentSerializer is aware of this wrapper,
        // so it can register the deferred attachments which we can extract later
        val wrapper = OutputStreamWrapper(baos, vertx)

        // Doing serialization
        mapper.writeValue(wrapper, obj)

        // Deserializing to map
        val map: Map<String, Any?> = mapper.readValue(baos.toByteArray(), Map::class.java).uncheckedCast()
        // Deferred attachments are here because we used OutputStreamWrapper and put there by AttachmentSerializer
        val deferred = wrapper.deferredAttachments


        for ((k, v) in map) {
            when (v) {
                null -> continue
                is Boolean -> if (v) add(FieldPart(k, v))
                is Map<*, *>, is List<*> -> add(FieldPart(k, mapper.writeValueAsString(v)))
                else -> add(FieldPart(k, v))
            }
        }

        for ((_, part) in deferred) {
            add(part)
        }
    })
}