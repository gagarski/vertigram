package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.InlineQuery
import ski.gagar.vertigram.telegram.types.SensitiveData
import java.time.Duration

private fun InlineQuery.InputMessageContent?.copyWithoutSensitiveData(): InlineQuery.InputMessageContent? =
    (this as? SensitiveData<*>)?.copyWithoutSensitiveData() as? InlineQuery.InputMessageContent ?: this

private fun InlineQuery.Result.copyWithoutSensitiveData(): InlineQuery.Result = when (this) {
    is InlineQuery.Result.Article -> copy(inputMessageContent = requireNotNull(inputMessageContent.copyWithoutSensitiveData()))
    is InlineQuery.Result.Audio -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Audio.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Contact -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Document -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Document.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Game -> this
    is InlineQuery.Result.Gif -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Gif.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Location -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Mpeg4Gif -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Mpeg4Gif.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Photo -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Photo.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Sticker.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Venue -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Video -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Video.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Voice -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
    is InlineQuery.Result.Voice.Cached -> copy(inputMessageContent = inputMessageContent.copyWithoutSensitiveData())
}

/**
 * Use this method to send answers to an inline query. No more than 50 results per query are allowed. On success, `true`
 * is returned.
 *
 * See Telegram's [answerInlineQuery](https://core.telegram.org/bots/api#answerinlinequery) documentation.
 */
@TelegramCodegen.Method
data class AnswerInlineQuery internal constructor(
    /** Unique identifier for the answered query. */
    val inlineQueryId: String,
    /** List of results for the inline query. */
    val results: List<InlineQuery.Result>,
    /** The maximum amount of time that the result of the inline query may be cached on the server. */
    val cacheTime: Duration? = null,
    /** Pass `true` if results may be cached on the server side only for the user that sent the query. */
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    /**
     * Pass the offset that a client should send in the next query with the same text to receive more results. Pass an
     * empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
     */
    val nextOffset: String? = null,
    /** Button to be shown above inline query results. */
    val button: InlineQuery.Result.Button? = null
) : JsonTelegramCallable<Boolean>(), SensitiveData<AnswerInlineQuery> {
    override fun copyWithoutSensitiveData() =
        copy(results = results.map(InlineQuery.Result::copyWithoutSensitiveData))
}
