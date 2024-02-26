package ski.gagar.vertigram.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.throttling.HasChatId
import ski.gagar.vertigram.throttling.Throttled
import ski.gagar.vertigram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Telegram [sendChatAction](https://core.telegram.org/bots/api#sendchataction) method.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@Throttled
data class SendChatAction(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    override val chatId: ChatId,
    val messageThreadId: Long? = null,
    val action: Action
) : JsonTelegramCallable<Boolean>(), HasChatId {
    /**
     * Telegram [ChatAction](https://core.telegram.org/bots/api#chataction) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    enum class Action {
        @JsonProperty("typing")
        TYPING,
        @JsonProperty("upload_photo")
        UPLOAD_PHOTO,
        @JsonProperty("record_video")
        RECORD_VIDEO,
        @JsonProperty("upload_video")
        UPLOAD_VIDEO,
        @JsonProperty("record_voice")
        RECORD_VOICE,
        @JsonProperty("upload_voice")
        UPLOAD_VOICE,
        @JsonProperty("upload_document")
        UPLOAD_DOCUMENT,
        @JsonProperty("choose_sticker")
        CHOOSE_STICKER,
        @JsonProperty("find_location")
        FIND_LOCATION,
        @JsonProperty("record_video_note")
        RECORD_VIDEO_NOTE,
        @JsonProperty("upload_video_note")
        UPLOAD_VIDEO_NOTE
    }

}
