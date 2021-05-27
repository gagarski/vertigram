package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod
object DeleteWebhook : JsonTgCallable<Boolean>()
