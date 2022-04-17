package ski.gagar.vxutil.vertigram.methods

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.ext.web.multipart.MultipartForm

sealed interface TgCallable<ReturnType>

interface JsonTgCallable<ReturnType> : TgCallable<ReturnType>

interface MultipartTgCallable<ReturnType> : TgCallable<ReturnType> {
    fun MultipartForm.doSerializeToMultipart(mapper: ObjectMapper = DatabindCodec.mapper())
    fun serializeToMultipart(mapper: ObjectMapper = DatabindCodec.mapper()): MultipartForm =
        MultipartForm.create().apply {
            this.doSerializeToMultipart(mapper)
        }
}
