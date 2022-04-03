package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.File

data class GetFile(val fileId: String) : JsonTgCallable<File>()
