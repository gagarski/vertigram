package ski.gagar.vertigram.telegram.types.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Telegram [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) method.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Method
object GetForumTopicIconStickers : JsonTelegramCallable<List<Sticker>>()
