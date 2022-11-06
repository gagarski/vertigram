package ski.gagar.vxutil.vertigram.throttling

import ski.gagar.vxutil.vertigram.types.ChatId

interface HasChatId {
    val chatId: ChatId?
}

interface HasChatIdLong {
    val chatId: Long?
}
