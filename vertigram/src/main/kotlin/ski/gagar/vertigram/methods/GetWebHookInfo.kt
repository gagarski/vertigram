package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.WebhookInfo

@TgMethod
object GetWebHookInfo : JsonTgCallable<WebhookInfo>()
