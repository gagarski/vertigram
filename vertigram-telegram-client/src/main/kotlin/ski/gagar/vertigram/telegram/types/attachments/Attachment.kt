package ski.gagar.vertigram.telegram.types.attachments

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.vertx.core.Vertx
import ski.gagar.vertigram.web.multipart.Part
import java.io.File

/**
 * A class representing Telegram attachment.
 *
 * It represents either `InputFile` type from Telegram API or a value of [ski.gagar.vertigram.telegram.types.InputMedia.media].
 *
 * Telegram uses `multipart/form-data` for sending attachments and Vertigram uses this content type for all methods
 * that can have files attached (even if they actually do not have).

 *
 * Vertigram uses all three methods to send the files, depending on the called Telegram method. It provides you two
 * implementations:
 *  - [StringAttachment] which allows you to attach URL or file id as a media
 *  - [FileAttachment] which allows you to attach a file from file system as a media
 *
 * If that's enough for you, just use these implementations where Vertigram expects an [Attachment] field, you don't
 * need to worry about anything else. You can also use convenience methods: [Attachment.Companion.fileId],
 * [Attachment.Companion.url] and [Attachment.Companion.file] to construct the instances of these classes.
 *
 * If you want to send some kind of "virtual" file (e.g. read from database or from event bus), you might want to
 * implement this interface, please see docs for the methods and [FileAttachment] implementation for reference.
 * In most cases, you want [AbstractFileAttachment] as a skeleton. You may also need to extend [Part] or use
 * [ski.gagar.vertigram.web.multipart.CloseableReadStreamPart].
 *
 * Please refer to the following parts of Telegram Bot API docs:
 *  - [InputMedia](https://core.telegram.org/bots/api#inputmedia)
 *  - [InputFile](https://core.telegram.org/bots/api#inputfile)
 *  - [InputSticker](https://core.telegram.org/bots/api#inputsticker)
 *
 *  @see Part
 */
@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
interface Attachment {
    /**
     * Return a string attachment representation of `this`.
     *
     * For URL or file id it's `this` itself, for file attachments, it's usually `attach://xxx` URL
     */
    fun getReference(referredField: String): StringAttachment

    /**
     * If the attachment is being sent using method (3), then return a [Part] with the attachment itself.
     *
     * For URL or file id attachments, `null` is returned.
     */
    fun getReferredPart(field: String, vertx: Vertx): Part?

    companion object
}

/**
 * A skeleton to implement custom [Attachment] support.
 */
abstract class AbstractFileAttachment : Attachment {
    override fun getReference(referredField: String): StringAttachment = StringAttachment("attach://$referredField")
    override fun getReferredPart(field: String, vertx: Vertx) = doAttach(field, vertx)


    /**
     * Implement this method to return the "meaningful" [Part] (i.e. file contents)
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
