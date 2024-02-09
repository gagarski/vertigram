package ski.gagar.vertigram.client

import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vertigram.methods.getFile
import ski.gagar.vertigram.methods.TgCallable
import ski.gagar.vertigram.util.TelegramNoFilePathException
import ski.gagar.vertigram.util.VertigramTypeHints
import ski.gagar.vertigram.util.getOrAssert
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER

abstract class AbstractTelegram : Telegram {
    protected val typeFactory: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

    override suspend fun <T> call(callable: TgCallable<T>): T =
        call(VertigramTypeHints.returnTypesByClass.getOrAssert(callable.javaClass), callable)

    override suspend fun downloadFileById(id: String, outputPath: String) {
        val path = getFile(id).filePath ?: throw TelegramNoFilePathException(id)
        downloadFile(path, outputPath)
    }
}
