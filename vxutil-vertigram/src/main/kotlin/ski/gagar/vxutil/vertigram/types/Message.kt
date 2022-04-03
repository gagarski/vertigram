package ski.gagar.vxutil.vertigram.types

import java.time.Instant

/**
 * Telegram type Message.
 */
data class Message(
    val messageId: Long,
    val from: User? = null,
    val senderChat: Chat? = null,
    val date: Instant,
    val chat: Chat,
    val forwardFrom: User? = null,
    val forwardFromChat: Chat? = null,
    val forwardFromMessageId: Long? = null,
    val forwardSignature: String? = null,
    val forwardSenderName: String? = null,
    val forwardDate: Instant? = null,
    val isAutomaticForward: Boolean = false,
    val replyToMessage: Message? = null,
    val editDate: Instant? = null,
    val hasProtectedContent: Boolean = false,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    val caption: String? = null,
    val captionEntities: List<MessageEntity>? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val game: Game? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val location: Location? = null,
    val newChatMembers: List<User>? = null,
    val leftChatMember: User? = null,
    val newChatTitle: String? = null,
    val newChatPhoto: List<PhotoSize>? = null,
    val deleteChatPhoto: Boolean = false,
    val groupChatCreated: Boolean = false,
    val supergroupChatCreated: Boolean = false,
    val channelChatCreated: Boolean = false,
    val messageAutoDeleteTimerChanged: Boolean = false,
    val migrateToChatId: Long? = null,
    val migrateFromChatId: Long? = null,
    val pinnedMessage: Message? = null,
    val invoice: Invoice? = null,
    val successfulPayment: SuccessfulPayment? = null,
    val connectedWebsite: String? = null,
    val passportData: PassportData? = null,
    val proximityAlertTriggered: ProximityAlertTriggered? = null,
    val voiceChatScheduled: VoiceChatScheduled? = null,
    val voiceChatStarted: VoiceChatStarted? = null,
    val voiceChatEnded: VoiceChatEnded? = null,
    val voiceChatParticipantsInvited: VoiceChatParticipantsInvited? = null,
    val replyMarkup: InlineKeyboardMarkup? = null

)

val Message.entitiesInstantiated: List<InstantiatedEntity>
    get() = entities?.map { InstantiatedEntity(it, this.text) } ?: listOf()

val Message.captionEntitiesInstantiated: List<InstantiatedEntity>
    get() = captionEntities?.map { InstantiatedEntity(it, this.text) } ?: listOf()
