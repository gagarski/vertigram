package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.File

data class GetFile(val fileId: String) : JsonTgCallable<File>()
