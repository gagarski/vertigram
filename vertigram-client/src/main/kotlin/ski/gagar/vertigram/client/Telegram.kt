package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import ski.gagar.vertigram.methods.TgCallable
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateType

interface Telegram {
    suspend fun getUpdates(
        offset: Long? = null,
        limit: Int? = null,
        allowedUpdates: List<UpdateType>? = null
    ): List<Update>

    suspend fun <T> call(callable: TgCallable<T>): T

    suspend fun <T> call(type: JavaType, callable: TgCallable<T>): T

    suspend fun downloadFile(path: String, outputPath: String)
    suspend fun downloadFileById(id: String, outputPath: String)
}
