package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.Message
import ski.gagar.vertigram.entities.ReplyMarkup

data class SendDice(
    val chatId: Long,
    val emoji: String = CUBE_EMOJI,
    val disableNotification: Boolean = false,
    val replyToMessageId: Long? = null,
    val replyMarkup: ReplyMarkup? = null
) : JsonTgCallable<Message>() {
    companion object {
        const val CUBE_EMOJI = "\uD83C\uDFB2"
        const val DART_EMOJI = "\uD83C\uDFAF"
    }
}