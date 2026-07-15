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
 * Telegram [answerInlineQuery](https://core.telegram.org/bots/api#answerinlinequery) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
data class AnswerInlineQuery internal constructor(
    val inlineQueryId: String,
    val results: List<InlineQuery.Result>,
    val cacheTime: Duration? = null,
    @get:JvmName("getIsPersonal")
    val isPersonal: Boolean = false,
    val nextOffset: String? = null,
    val button: InlineQuery.Result.Button? = null
) : JsonTelegramCallable<Boolean>(), SensitiveData<AnswerInlineQuery> {
    override fun copyWithoutSensitiveData() =
        copy(results = results.map(InlineQuery.Result::copyWithoutSensitiveData))
}
