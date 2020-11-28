package ski.gagar.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.multipart.MultipartForm

sealed class TgCallable<ReturnType>

abstract class JsonTgCallable<ReturnType> : TgCallable<ReturnType>()

abstract class MultipartTgCallable<ReturnType> : TgCallable<ReturnType>() {
    abstract fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper = DatabindCodec.mapper())
    fun serializeToMultipart(mapper: ObjectMapper = DatabindCodec.mapper()): MultipartForm =
        MultipartForm.create().apply {
            this.doSerializeToMultipart(mapper)
        }
}
