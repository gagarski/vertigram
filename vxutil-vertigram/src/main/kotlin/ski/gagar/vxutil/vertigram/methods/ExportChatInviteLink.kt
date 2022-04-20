package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.types.ChatId

@TgMethod
data class ExportChatInviteLink(
    val chatId: ChatId
) : JsonTgCallable<String>()
