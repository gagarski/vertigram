package ski.gagar.vertigram.throttling

import ski.gagar.vertigram.types.util.ChatId

/**
 * If implemented by [Throttled] [ski.gagar.vertigram.methods.TelegramCallable], then per-chat throttling is applied
 */
interface HasChatId {
    val chatId: ChatId?
}

/**
 * If implemented by [Throttled] [ski.gagar.vertigram.methods.TelegramCallable], then per-chat throttling is applied
 */
interface HasChatIdLong {
    val chatId: Long?
}
