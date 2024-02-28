package ski.gagar.vertigram.telegram.client

import ski.gagar.vertigram.telegram.exceptions.TelegramNoFilePathException
import ski.gagar.vertigram.telegram.methods.TelegramCallable
import ski.gagar.vertigram.telegram.methods.getFile
import ski.gagar.vertigram.telegram.types.Update

/**
 * Telegram client interface.
 *
 * Allows to [call] methods and defines higher-level abstractions for some method calls
 */
interface Telegram {
    /**
     * Call `getUpdates` Telegram method.
     *
     * `timeout` is not a parameter, timeous should be managed by the implementation
     *
     * The present parameters are passed to `getUpdates` as is,
     * see [getUpdates docs](https://core.telegram.org/bots/api#getupdates) for up-to-date details.
     */
    suspend fun getUpdates(
        offset: Long? = null,
        limit: Int? = null,
        allowedUpdates: List<Update.Type>
    ): List<Update<*>>

    /**
     * Call Telegram method
     *
     * @see TelegramCallable
     */
    suspend fun <T> call(callable: TelegramCallable<T>): T

    /**
     * Download file from Telegram
     *
     * https://api.telegram.org/file/bot<token>/<file_path> link is used to download.
     */
    suspend fun downloadFile(path: String, outputPath: String)

    /**
     * Shortcut for [Telegram.getFile] + [downloadFile].
     */
    suspend fun downloadFileById(id: String, outputPath: String) {
        val path = getFile(fileId = id).filePath ?: throw TelegramNoFilePathException(id)
        downloadFile(path, outputPath)
    }
}
