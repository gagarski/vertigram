package ski.gagar.vertigram.telegram.types.attachments

import io.vertx.core.Vertx

/**
 * An [Attachment] referencing an HTTP URL or an existing Telegram file ID.
 *
 * Its [url] is written directly into the Telegram request and no multipart upload part is created.
 */
data class StringAttachment internal constructor(val url: String) : Attachment {
    override fun getReference(referredField: String) = this

    override fun getReferredPart(field: String, vertx: Vertx): Nothing? = null

}
