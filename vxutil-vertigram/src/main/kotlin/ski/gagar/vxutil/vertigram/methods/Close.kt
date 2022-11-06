package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod

@TgMethod(kotlinMethodName = "closeApi")
object Close : JsonTgCallable<Boolean>()
