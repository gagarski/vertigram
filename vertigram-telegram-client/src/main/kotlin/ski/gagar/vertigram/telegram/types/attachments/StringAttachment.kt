package ski.gagar.vertigram.telegram.types.attachments

import io.vertx.core.Vertx

/**
 * An implementation of [Attachment] allowing to attach URL of file id.
 */
data class StringAttachment internal constructor(val url: String) : Attachment {
    override fun getReference(referredField: String) = this

    override fun getReferredPart(field: String, vertx: Vertx): Nothing? = null

}
