package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Sticker

/**
 * Telegram [getForumTopicIconStickers](https://core.telegram.org/bots/api#getforumtopiciconstickers) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
object GetForumTopicIconStickers : JsonTelegramCallable<List<Sticker>>()
