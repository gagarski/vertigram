package ski.gagar.vxutil.vertigram.client

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vxutil.vertigram.getFile
import ski.gagar.vxutil.vertigram.methods.TgCallable
import ski.gagar.vxutil.vertigram.util.TelegramNoFilePathException
import ski.gagar.vxutil.vertigram.util.TypeHints
import ski.gagar.vxutil.vertigram.util.getOrAssert
import ski.gagar.vxutil.vertigram.util.json.TELEGRAM_JSON_MAPPER

abstract class AbstractTelegram : Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

    override suspend fun <T> call(callable: TgCallable<T>): T =
        call(TypeHints.returnTypesByClass.getOrAssert(callable.javaClass), callable)

    abstract suspend fun <T> call(
        type: JavaType,
        callable: TgCallable<T>
    ): T

    override suspend fun downloadFileById(id: String, outputPath: String) {
        val path = getFile(id).filePath ?: throw TelegramNoFilePathException(id)
        downloadFile(path, outputPath)
    }
}
