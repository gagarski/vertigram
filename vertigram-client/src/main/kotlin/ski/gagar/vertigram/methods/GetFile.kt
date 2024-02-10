package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.File

data class GetFile(val fileId: String) : JsonTelegramCallable<File>()
