package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vertigram.entities.Update
import ski.gagar.vertigram.entities.requests.TgCallable
import ski.gagar.vertigram.util.TELEGRAM_JSON_MAPPER
import ski.gagar.vertigram.entities.requests.GetFile
import ski.gagar.vertigram.util.TelegramNoFilePathException
import ski.gagar.vertigram.util.TypeHints
import ski.gagar.vertigram.util.getOrAssert

abstract class Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory
    abstract suspend fun getUpdates(offset: Long? = null, limit: Long? = null): List<Update>


    suspend fun <T> call(callable: TgCallable<T>): T =
        call(TypeHints.returnTypesByClass.getOrAssert(callable.javaClass), callable)

    abstract suspend fun <T> call(
        type: JavaType,
        callable: TgCallable<T>
    ): T

    abstract suspend fun downloadFile(path: String, outputPath: String)

    suspend fun downloadFileById(id: String, outputPath: String) {
        val path = call(GetFile(id)).filePath ?: throw TelegramNoFilePathException
        downloadFile(path, outputPath)
    }
}
