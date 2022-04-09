package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatPermissions

data class ExportChatInviteLink(
    val chatId: ChatId
) : JsonTgCallable<String>()
