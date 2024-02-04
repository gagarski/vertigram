package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Me

@TgMethod
object GetMe : JsonTgCallable<Me>()
