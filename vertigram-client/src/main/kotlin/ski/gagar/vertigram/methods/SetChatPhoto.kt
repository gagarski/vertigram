package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.ChatId
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.util.multipart.TgMedia

@TgMethod
@Throttled
data class SetChatPhoto(
    override val chatId: ChatId,
    @TgMedia
    val photo: Attachment
) : MultipartTgCallable<Message>(), HasChatId
