package ski.gagar.vertigram.telegram.throttling

import ski.gagar.vertigram.telegram.types.util.ChatId

/**
 * If implemented by [Throttled] [ski.gagar.vertigram.telegram.methods.TelegramCallable], then per-chat throttling is applied
 */
interface HasChatId {
    val chatId: ChatId?
}

/**
 * If implemented by [Throttled] [ski.gagar.vertigram.telegram.methods.TelegramCallable], then per-chat throttling is applied
 */
interface HasChatIdLong {
    val chatId: Long?
}
