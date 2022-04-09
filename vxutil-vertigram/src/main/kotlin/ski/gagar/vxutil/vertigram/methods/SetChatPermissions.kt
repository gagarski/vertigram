package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatPermissions

data class SetChatPermissions(
    val chatId: ChatId,
    val permissions: ChatPermissions
) : JsonTgCallable<Boolean>()
