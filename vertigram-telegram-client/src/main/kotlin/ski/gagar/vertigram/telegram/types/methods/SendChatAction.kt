package ski.gagar.vertigram.telegram.types.methods

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.throttling.HasChatId
import ski.gagar.vertigram.telegram.throttling.Throttled
import ski.gagar.vertigram.telegram.types.util.ChatId
import ski.gagar.vertigram.util.NoPosArgs

/**
 * Use this method when you need to tell the user that something is happening on the bot's side.
 *
 * See Telegram's [sendChatAction](https://core.telegram.org/bots/api#sendchataction) documentation.
 */
@Throttled
@TelegramCodegen.Method
data class SendChatAction internal constructor(
    /** Unique identifier of the business connection on behalf of which the message will be sent. */
    val businessConnectionId: String? = null,
    /** Unique identifier for the target chat or username of the target bot, supergroup, or channel. */
    override val chatId: ChatId,
    /** Unique identifier for the target message thread. */
    val messageThreadId: Long? = null,
    /** Type of action to broadcast. */
    val action: Action
) : JsonTelegramCallable<Boolean>(), HasChatId {
    /**
     * Type of action to broadcast.
     */
    enum class Action {
        /** The bot is typing a text message. */
        @JsonProperty("typing")
        TYPING,
        /** The bot is uploading a photo. */
        @JsonProperty("upload_photo")
        UPLOAD_PHOTO,
        /** The bot is recording a video. */
        @JsonProperty("record_video")
        RECORD_VIDEO,
        /** The bot is uploading a video. */
        @JsonProperty("upload_video")
        UPLOAD_VIDEO,
        /** The bot is recording a voice message. */
        @JsonProperty("record_voice")
        RECORD_VOICE,
        /** The bot is uploading a voice message. */
        @JsonProperty("upload_voice")
        UPLOAD_VOICE,
        /** The bot is uploading a general file. */
        @JsonProperty("upload_document")
        UPLOAD_DOCUMENT,
        /** The bot is choosing a sticker. */
        @JsonProperty("choose_sticker")
        CHOOSE_STICKER,
        /** The bot is finding a location. */
        @JsonProperty("find_location")
        FIND_LOCATION,
        /** The bot is recording a video note. */
        @JsonProperty("record_video_note")
        RECORD_VIDEO_NOTE,
        /** The bot is uploading a video note. */
        @JsonProperty("upload_video_note")
        UPLOAD_VIDEO_NOTE
    }

}
