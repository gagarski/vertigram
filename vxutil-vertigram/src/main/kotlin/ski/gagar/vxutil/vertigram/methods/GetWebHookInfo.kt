package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.WebhookInfo

@TgMethod
object GetWebHookInfo : JsonTgCallable<WebhookInfo>()