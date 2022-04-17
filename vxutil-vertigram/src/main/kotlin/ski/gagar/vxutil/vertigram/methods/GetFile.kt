package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.File

@TgMethod
data class GetFile(val fileId: String) : JsonTgCallable<File>
