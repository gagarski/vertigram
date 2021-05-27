package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.entities.User

@TgMethod
object GetMe : JsonTgCallable<User>()
