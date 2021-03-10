package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.File

data class GetFile(val fileId: String) : JsonTgCallable<File>()
