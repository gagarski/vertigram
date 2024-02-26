package ski.gagar.vertigram.telegram.types.attachments

import io.vertx.core.Vertx
import ski.gagar.vertigram.web.multipart.FieldPart

/**
 * An implementation of [Attachment] allowing to attach URL of file id.
 */
data class StringAttachment(val url: String) : Attachment {
    override fun getReference(referredField: String) = this

    override fun getReferredPart(field: String, vertx: Vertx): Nothing? = null

    override fun getPart(field: String, vertx: Vertx): FieldPart = FieldPart(field, url)
}
