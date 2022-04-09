package ski.gagar.vxutil.vertigram.types.attachments

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import io.vertx.ext.web.multipart.MultipartForm
import java.io.File

@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="@class")
interface Attachment {
    fun attachDirectly(form: MultipartForm, field: String)
    fun attachIndirectly(form: MultipartForm, field: String)
    fun getIndirectUrl(field: String): String
    fun getIndirectAttachment(field: String): UrlAttachment
    companion object
}

fun MultipartForm.attachDirectly(field: String, attachment: Attachment?) =
    attachment?.attachDirectly(this, field)

fun MultipartForm.attachIndirectly(field: String, attachment: Attachment?) =
    attachment?.attachIndirectly(this, field)

fun Attachment.Companion.url(url: String) = UrlAttachment(url)
fun Attachment.Companion.file(file: File) = FileAttachment(file)

fun String.toAttachment() = UrlAttachment(this)
fun File.toAttachment() = FileAttachment(this)
