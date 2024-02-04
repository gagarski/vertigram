package ski.gagar.vertigram.throttling

import ski.gagar.vertigram.types.ChatId

interface HasChatId {
    val chatId: ChatId?
}

interface HasChatIdLong {
    val chatId: Long?
}
