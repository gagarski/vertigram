package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vxutil.vertigram.throttling.HasChatId
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.ChatPermissions
import java.time.Instant

@TgMethod
data class RestrictChatMember(
    override val chatId: ChatId,
    val userId: Long,
    val permissions: ChatPermissions,
    val untilDate: Instant? = null
) : JsonTgCallable<Boolean>(), HasChatId
