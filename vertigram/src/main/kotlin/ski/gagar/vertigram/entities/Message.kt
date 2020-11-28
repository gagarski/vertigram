package ski.gagar.vertigram.entities

import java.time.Instant

data class Message(
    val messageId: Long,
    val from: User? = null,
    val date: Instant,
    val chat: Chat,
    val forwardFrom: User? = null,
    val forwardFromChat: Chat? = null,
    val forwardFromMessageId: Long? = null,
    val forwardSignature: String? = null,
    val forwardSenderName: String? = null,
    val forwardDate: Instant? = null,
    val replyToMessage: Message? = null,
    val editDate: Instant? = null,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val captionEntities: List<MessageEntity>? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val anumation: Animation? = null,
    val game: Game? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val video: Video? = null,
    val voice: Voice? = null,
    val videoNote: VideoNote? = null,
    val caption: String? = null,
    val contact: Contact? = null,
    val location: Location? = null,
    val venue: Venue? = null,
    val poll: Poll? = null,
    val dice: Dice? = null,
    val newChatMembers: List<User>? = null,
    val leftChatMember: User? = null,
    val newChatTitle: String? = null,
    val newChatPhoto: List<PhotoSize>? = null,
    val deleteChatPhoto: Boolean = false,
    val groupChatCreated: Boolean = false,
    val supergroupChatCreated: Boolean = false,
    val channelChatCreated: Boolean = false,
    val migrateToChatId: Long? = null,
    val migrateFromChatId: Long? = null,
    val pinnedMessage: Message? = null,
    val invoice: Invoice? = null,
    val successfulPayment: SuccessfulPayment? = null,
    val connectedWebsite: String? = null,
    // TODO passportData
    val replyMarkup: InlineKeyboardMarkup? = null

)

val Message.instantiatedEntities: List<InstantiatedEntity>
    get() = entities?.map { InstantiatedEntity(it, this.text) } ?: listOf()

val Message.instantiatedCaptionEntities: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.text) } ?: listOf()