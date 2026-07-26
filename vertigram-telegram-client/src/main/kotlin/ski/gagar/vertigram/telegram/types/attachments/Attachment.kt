package ski.gagar.vertigram.telegram.types.attachments

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.vertx.core.Vertx
import ski.gagar.vertigram.web.multipart.Part
import java.io.File

/**
 * A Telegram attachment and the public extension point for custom upload sources.
 *
 * An attachment participates in multipart serialization in two phases:
 *
 *  1. [getReference] produces the string written into the JSON portion of the request. A directly uploaded file
 *     normally uses an `attach://<field>` reference; an existing Telegram file ID or an HTTP URL uses the value
 *     itself.
 *  2. [getReferredPart] optionally produces the multipart [Part] whose field name matches that reference.
 *
 * The [Attachment] itself is a polymorphic, JSON-serializable value. A custom implementation should therefore keep
 * serializable information needed to locate its data, such as a path or database object ID, rather than an open file,
 * a live stream, or a provider lambda. The [Part] returned later is transient and is not JSON-serialized.
 *
 * Vertigram provides [StringAttachment] for Telegram file IDs and URLs and [FileAttachment] for local files.
 * Use [AbstractFileAttachment] as the usual base for a custom file backed by a database, object store, event bus, or
 * another runtime source. Returning [Part] is intentionally a low-level SPI: implementations may control the
 * filename, content type, headers, stream creation, and resource ownership. Prefer the built-in multipart part
 * implementations unless custom framing is actually required.
 *
 * Implementations and their serialized state must be available to the process that ultimately sends the request.
 * In particular, a local path is only useful when that process can access the same filesystem.
 *
 * See Telegram's [InputMedia](https://core.telegram.org/bots/api#inputmedia),
 * [InputFile](https://core.telegram.org/bots/api#inputfile), and
 * [InputSticker](https://core.telegram.org/bots/api#inputsticker) documentation.
 *
 * @see AbstractFileAttachment
 * @see Part
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
interface Attachment {
    /**
     * Returns the value written into the JSON portion of the multipart request.
     *
     * [referredField] is the generated multipart field name reserved for this occurrence of the attachment. Uploading
     * implementations should normally return `attach://<referredField>`; references to an existing Telegram file or
     * URL should return that reference unchanged.
     */
    fun getReference(referredField: String): StringAttachment

    /**
     * Creates the multipart body part for this occurrence of the attachment, or returns `null` when no upload is
     * required.
     *
     * This method is called only while constructing a multipart request, after [getReference]. [field] is the same
     * generated name passed to [getReference], and must be used as the returned part's form field name.
     *
     * [vertx] identifies the runtime that will send the request and can be used to access its filesystem or
     * application-specific shared services. Acquire external resources lazily from the returned [Part], not while
     * this method is running. Return a fresh part for each invocation; multipart parts and their streams should be
     * treated as single-transmission objects.
     *
     * The part's acquired stream is released after successful transmission, failure, or cancellation according to
     * that part's ownership contract. [StringAttachment] returns `null` because Telegram resolves its file ID or URL
     * without an upload.
     */
    fun getReferredPart(field: String, vertx: Vertx): Part?

    companion object
}

/**
 * Base class for an [Attachment] that uploads a multipart part.
 *
 * It supplies the matching `attach://<field>` reference and delegates creation of the transient upload part to
 * [doAttach]. Subclasses should retain only serializable source descriptors and resolve live resources lazily using
 * the provided [Vertx] instance.
 */
abstract class AbstractFileAttachment : Attachment {
    override fun getReference(referredField: String): StringAttachment = StringAttachment("attach://$referredField")
    override fun getReferredPart(field: String, vertx: Vertx) = doAttach(field, vertx)


    /**
     * Creates a fresh multipart part containing this attachment's data.
     *
     * Use [field] as the part's form field name. The returned part owns and closes only the resources specified by
     * its concrete ownership contract.
     */
    protected abstract fun doAttach(field: String, vertx: Vertx): Part
}

/**
 * Convenience method to create [StringAttachment]
 */
fun Attachment.Companion.fileId(fileId: String) = StringAttachment(fileId)

/**
 * Convenience method to create [StringAttachment]
 */
fun Attachment.Companion.url(url: String) = StringAttachment(url)

/**
 * Convenience method to create [FileAttachment]
 */
fun Attachment.Companion.file(file: File) = FileAttachment(file)

/**
 * Convenience method to create [StringAttachment]
 */
fun String.toAttachment() = StringAttachment(this)

/**
 * Convenience method to create [FileAttachment]
 */
fun File.toAttachment() = FileAttachment(this)
