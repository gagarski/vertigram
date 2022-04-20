package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.InlineQueryResult
import ski.gagar.vxutil.vertigram.types.SentWebAppMessage

@TgMethod
data class AnswerWebAppQuery(
    val webAppQueryId: String,
    val result: List<InlineQueryResult>
) : JsonTgCallable<SentWebAppMessage>()
