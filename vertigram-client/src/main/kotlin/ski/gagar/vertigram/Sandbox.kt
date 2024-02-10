package ski.gagar.vertigram

import ski.gagar.vertigram.methods.CopyMessage
import ski.gagar.vertigram.richtext.CaptionWithEntities
import ski.gagar.vertigram.types.toChatId
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER

fun main() {
    val cm = CopyMessage(
        chatId = "1".toChatId(),
        fromChatId = "2".toChatId(),
        messageId = 1,
        richCaption = CaptionWithEntities(
            caption = "xxx"
        )
    )
    println(TELEGRAM_JSON_MAPPER.writeValueAsString(cm))
}