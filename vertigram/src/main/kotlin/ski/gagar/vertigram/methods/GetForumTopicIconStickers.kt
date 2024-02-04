package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Sticker

@TgMethod
object GetForumTopicIconStickers : JsonTgCallable<List<Sticker>>()
