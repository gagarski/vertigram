package ski.gagar.vxutil.web

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.codec.http.HttpHeaderValues
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.multipart.MultipartForm
import java.io.File
import java.nio.file.Files

fun MultipartForm.attributeIfNotNull(key: String, value: Any?) = apply {
    value?.let {
        attribute(key, "$value")
    }
}

fun MultipartForm.jsonAttributeIfNotNull(key: String, value: Any?, mapper: ObjectMapper = DatabindCodec.mapper()) = apply {
    value?.let {
        attribute(key, mapper.writeValueAsString(value))
    }
}

fun MultipartForm.attributeIfTrue(key: String, value: Boolean) = apply {
    if (value) {
        attribute(key, "$value")
    }
}

fun MultipartForm.binaryFileUploadIfNotNull(key: String, file: File?) = apply {
    file?.let {
        binaryFileUpload(
            key,
            "${it.toPath().fileName}",
            "${it.toPath()}",
            Files.probeContentType(it.toPath()) ?: HttpHeaderValues.APPLICATION_OCTET_STREAM.toString()
        )
    }
}
