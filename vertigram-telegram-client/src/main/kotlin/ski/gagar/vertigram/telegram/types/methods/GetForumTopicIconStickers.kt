package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Use this method to get custom emoji stickers that can be used as a forum topic icon by any user.
 *
 * See Telegram's
 * [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) documentation.
 */
@TelegramCodegen.Method
object GetForumTopicIconStickers : JsonTelegramCallable<List<Sticker>>()
