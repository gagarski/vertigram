package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.File

@TgMethod
data class GetFile(val fileId: String) : JsonTgCallable<File>()
