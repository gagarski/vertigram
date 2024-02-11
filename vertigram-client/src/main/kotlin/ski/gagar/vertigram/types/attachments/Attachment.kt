package ski.gagar.vertigram.types.attachments

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.vertx.core.Vertx
import ski.gagar.vertigram.web.multipart.Part
import java.io.File

@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
interface Attachment {
    fun getReference(referredField: String): UrlAttachment
    fun getReferredPart(field: String, vertx: Vertx): Part?
    fun getPart(field: String, vertx: Vertx): Part
    companion object
}
fun Attachment.Companion.fileId(fileId: String) = UrlAttachment(fileId)
fun Attachment.Companion.url(url: String) = UrlAttachment(url)
fun Attachment.Companion.file(file: File) = FileAttachment(file)

fun String.toAttachment() = UrlAttachment(this)
fun File.toAttachment() = FileAttachment(this)
