package ski.gagar.vertigram.telegram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.Sticker

/**
 * Telegram [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@TelegramCodegen
object GetForumTopicIconStickers : JsonTelegramCallable<List<Sticker>>()
