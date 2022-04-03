package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.Message
import ski.gagar.vxutil.vertigram.types.ReplyMarkup

data class SendDice(
    val chatId: Long,
    val emoji: String = CUBE_EMOJI,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : ski.gagar.vxutil.vertigram.methods.JsonTgCallable<Message>() {
    companion object {
        const val CUBE_EMOJI = "\uD83C\uDFB2"
        const val DART_EMOJI = "\uD83C\uDFAF"
    }
}
