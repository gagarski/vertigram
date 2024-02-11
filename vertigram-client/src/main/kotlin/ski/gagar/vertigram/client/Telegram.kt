package ski.gagar.vertigram.client

import ski.gagar.vertigram.methods.TelegramCallable
import ski.gagar.vertigram.methods.getFile
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.util.TelegramNoFilePathException

interface Telegram {
    suspend fun getUpdates(
        offset: Long? = null,
        limit: Int? = null,
        allowedUpdates: List<UpdateType>? = null
    ): List<Update>

    suspend fun <T> call(callable: TelegramCallable<T>): T

    suspend fun downloadFile(path: String, outputPath: String)

    suspend fun downloadFileById(id: String, outputPath: String) {
        val path = getFile(fileId = id).filePath ?: throw TelegramNoFilePathException(id)
        downloadFile(path, outputPath)
    }
}
