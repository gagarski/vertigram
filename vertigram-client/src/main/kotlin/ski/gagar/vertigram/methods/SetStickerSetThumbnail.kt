package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.attachments.Attachment
import ski.gagar.vertigram.annotations.TelegramMedia

data class SetStickerSetThumbnail(
    val name: String,
    val userId: Long,
    @TelegramMedia
    val thumbnail: Attachment
) : MultipartTelegramCallable<Message>()
