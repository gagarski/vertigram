package ski.gagar.vertigram.entities.requests

import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.ext.web.multipart.MultipartForm
import ski.gagar.vertigram.entities.ChatMember
import ski.gagar.vertigram.entities.MaskPosition
import ski.gagar.vertigram.entities.StickerSet
import ski.gagar.vxutil.attributeIfNotNull
import ski.gagar.vxutil.attributeIfTrue
import ski.gagar.vxutil.binaryFileUploadIfNotNull
import ski.gagar.vxutil.jsonAttributeIfNotNull
import java.io.File

data class GetStickerSet(
    val name: String
) : JsonTgCallable<StickerSet>()
