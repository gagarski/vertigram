package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.util.TgEnumName


data class SendChatAction(
    val chatId: Long,
    val action: ChatAction
) : JsonTgCallable<Boolean>()


enum class ChatAction {
    @TgEnumName("typing")
    TYPING,
    @TgEnumName("upload_photo")
    UPLOAD_PHOTO,
    @TgEnumName("record_video")
    RECORD_VIDEO,
    @TgEnumName("upload_video")
    UPLOAD_VIDEO,
    @TgEnumName("record_audio")
    RECORD_AUDIO,
    @TgEnumName("upload_audio")
    UPLOAD_AUDIO,
    @TgEnumName("upload_document")
    UPLOAD_DOCUMENT,
    @TgEnumName("find_location")
    FIND_LOCATION,
    @TgEnumName("record_video_note")
    RECORD_VIDEO_NOTE,
    @TgEnumName("upload_video_note")
    UPLOAD_VIDEO_NOTE
}
