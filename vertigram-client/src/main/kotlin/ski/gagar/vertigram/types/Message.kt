package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.types.colors.RgbColor
import ski.gagar.vertigram.types.richtext.HasOptionalCaptionWithEntities
import ski.gagar.vertigram.types.richtext.HasOptionalTextWithEntities
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Duration
import java.time.Instant

/**
 * Telegram [Message](https://core.telegram.org/bots/api#message) type.
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
data class Message(
    @JsonIgnore
    private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
    val messageId: Long,
    val messageThreadId: Long? = null,
    val from: User? = null,
    val senderChat: Chat? = null,
    // sender_boost_count
    val date: Instant,
    val chat: Chat,
    val forwardOrigin: Origin? = null,
    @get:JvmName("getIsTopicMessage")
    val isTopicMessage: Boolean = false,
    @get:JvmName("getIsAutomaticForward")
    val isAutomaticForward: Boolean = false,
    val replyToMessage: Message? = null,
    val externalReply: ExternalReplyInfo? = null,
    val quote: TextQuote? = null,
    // reply_to_story
    val viaBot: User? = null,
    val editDate: Instant? = null,
    val hasProtectedContent: Boolean = false,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    override val text: String? = null,
    override val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    override val caption: String? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val hasMediaSpoiler: Boolean = false,
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
    val messageAutoDeleteTimerChanged: Service.MessageAutoDeleteTimerChanged? = null,
    val migrateToChatId: Long? = null,
    val migrateFromChatId: Long? = null,
    val pinnedMessage: Message? = null,
    val invoice: Invoice? = null,
    val successfulPayment: Service.SuccessfulPayment? = null,
    val usersShared: Service.UsersShared? = null,
    val chatShared: Service.ChatShared? = null,
    val connectedWebsite: String? = null,
    val writeAccessAllowed: Service.WriteAccessAllowed? = null,
    val passportData: PassportData? = null,
    val proximityAlertTriggered: Service.ProximityAlertTriggered? = null,
    // boost_added
    val forumTopicCreated: Service.ForumTopic.Created? = null,
    val forumTopicEdited: Service.ForumTopic.Edited? = null,
    val forumTopicClosed: Service.ForumTopic.Closed? = null,
    val forumTopicReopened: Service.ForumTopic.Reopened? = null,
    val generalForumTopicHidden: Service.ForumTopic.GeneralHidden? = null,
    val generalForumTopicUnhidden: Service.ForumTopic.GeneralUnhidden? = null,
    val giveawayCreated: Service.Giveaway.Created? = null,
    val giveaway: Giveaway? = null,
    val giveawayWinners: Giveaway.Winners? = null,
    val giveawayCompleted: Service.Giveaway.Completed? = null,
    val videoChatScheduled: Service.VideoChat.Scheduled? = null,
    val videoChatStarted: Service.VideoChat.Started? = null,
    val videoChatEnded: Service.VideoChat.Ended? = null,
    val videoChatParticipantsInvited: Service.VideoChat.ParticipantsInvited? = null,
    val webAppData: Service.WebAppData? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
) : HasOptionalTextWithEntities, HasOptionalCaptionWithEntities {
    /**
     * Telegram [ExternalReplyInfo](https://core.telegram.org/bots/api#externalreplyinfo) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class ExternalReplyInfo(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val origin: Message.Origin,
        val chat: Chat? = null,
        val messageId: Long? = null,
        val linkPreviewOptions: LinkPreviewOptions? = null,
        val animation: Animation? = null,
        val audio: Audio? = null,
        val document: Document? = null,
        val photo: List<PhotoSize>? = null,
        val sticker: Sticker? = null,
        val story: Story? = null,
        val video: Video? = null,
        val videoNote: VideoNote? = null,
        val voice: Voice? = null,
        val hasMediaSpoiler: Boolean = false,
        val contact: Contact? = null,
        val dice: Dice? = null,
        val game: Game? = null,
        val giveaway: Giveaway? = null,
        val giveawayWinners: Giveaway.Winners? = null,
        val invoice: Invoice? = null,
        val location: Location? = null,
        val poll: Poll? = null,
        val venue: Venue? = null
    )


    /**
     * Telegram [MessageOrigin](https://core.telegram.org/bots/api#messageorigin) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
    @JsonSubTypes(
        JsonSubTypes.Type(value = Origin.User::class, name = Origin.Type.USER_STR),
        JsonSubTypes.Type(value = Origin.HiddenUser::class, name = Origin.Type.HIDDEN_USER_STR),
        JsonSubTypes.Type(value = Origin.Chat::class, name = Origin.Type.CHAT_STR),
        JsonSubTypes.Type(value = Origin.Channel::class, name = Origin.Type.CHANNEL_STR)
    )
    sealed interface Origin {
        val type: Type

        /**
         * Telegram [MessageOriginUser](https://core.telegram.org/bots/api#messageoriginuser) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class User(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val date: Instant,
            val senderUser: ski.gagar.vertigram.types.User
        ) : Origin {
            override val type: Type = Type.USER
        }

        /**
         * Telegram [MessageOriginHiddenUser](https://core.telegram.org/bots/api#messageoriginhiddenuser) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class HiddenUser(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val date: Instant,
            val senderUserName: String
        ) : Origin {
            override val type: Type = Type.HIDDEN_USER
        }

        /**
         * Telegram [MessageOriginChat](https://core.telegram.org/bots/api#messageoriginchat) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Chat(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val date: Instant,
            val senderChat: ski.gagar.vertigram.types.Chat,
            val authorSignature: String? = null
        ) : Origin {
            override val type: Type = Type.CHAT
        }

        /**
         * Telegram [MessageOriginChannel](https://core.telegram.org/bots/api#messageoriginchannel) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Channel(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val date: Instant,
            val chat: ski.gagar.vertigram.types.Chat,
            val messageId: Long,
            val authorSignature: String? = null
        ) : Origin {
            override val type: Type = Type.CHANNEL
        }

        /**
         *  Value for [type].
         */
        enum class Type {
            @JsonProperty(USER_STR)
            USER,
            @JsonProperty(HIDDEN_USER_STR)
            HIDDEN_USER,
            @JsonProperty(CHAT_STR)
            CHAT,
            @JsonProperty(CHANNEL_STR)
            CHANNEL;

            companion object {
                const val USER_STR = "user"
                const val HIDDEN_USER_STR = "hidden_user"
                const val CHAT_STR = "chat"
                const val CHANNEL_STR = "channel"
            }
        }
    }

    /**
     * Telegram [TextQuote](https://core.telegram.org/bots/api#textquote) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class TextQuote(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        override val text: String,
        val position: Int,
        override val entities: List<MessageEntity>? = null,
        @get:JvmName("getIsManual")
        val isManual: Boolean = false
    ) : HasOptionalTextWithEntities

    /**
     * Telegram [LinkPreviewOptions](https://core.telegram.org/bots/api#linkpreviewoptions) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class LinkPreviewOptions(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        @get:JvmName("getIsDisabled")
        val isDisabled: Boolean = false,
        val url: String? = null,
        val preferSmallMedia: Boolean = false,
        val preferLargeMedia: Boolean = false,
        val showAboveText: Boolean = false
    )

    /**
     * Contains types, which act only as fields of [Message] and represent service messages according to docs.
     */
    object Service {
        /**
         * Telegram [MessageAutoDeleteTimerChanged](https://core.telegram.org/bots/api#messageautodeletetimerchanged) type.
         *
         * The `Message` prefix for the nested class might look weird. This class is nested because
         * it appears only as a field of message, It's `Message.MessageAutoDeleteTimerChanged`
         * (not `Message.AutoDeleteTimerChanged`) to denote the fact that it changes auto-delete timer for all the
         * messages, not the one it belongs to as a field.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class MessageAutoDeleteTimerChanged(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val messageAutoDeleteTime: Duration
        )

        /**
         * Telegram [SuccessfulPayment](https://core.telegram.org/bots/api#successfulpayment) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class SuccessfulPayment(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val currency: String,
            val totalAmount: Int,
            val invoicePayload: String,
            val shippingOptionId: String? = null,
            val orderInfo: OrderInfo? = null,
            val telegramPaymentChargeId: String,
            val providerPaymentChargeId: String
        )

        /**
         * Telegram [UsersShared](https://core.telegram.org/bots/api#usersshared) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class UsersShared(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val requestId: Long,
            val userIds: List<Long>
        )

        /**
         * Telegram [ChatShared](https://core.telegram.org/bots/api#chatshared) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class ChatShared(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val requestId: Long,
            val chatId: Long
        )

        /**
         * Telegram [WriteAccessAllowed](https://core.telegram.org/bots/api#writeaccessallowed) type.
         *
         * Subtypes (which are nested) are synthetic subtypes representing cases when exactly one of the original type field
         * is present.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
        @JsonSubTypes(
            JsonSubTypes.Type(WriteAccessAllowed.FromRequest::class),
            JsonSubTypes.Type(WriteAccessAllowed.WebApp::class),
            JsonSubTypes.Type(WriteAccessAllowed.FromAttachmentMenu::class),
        )
        interface WriteAccessAllowed {
            /**
             * Case when the access was granted after the user accepted an explicit request from a Web App sent by the method
             * `requestWriteAccess`.
             */
            data class FromRequest(val fromRequest: Boolean = true) : WriteAccessAllowed {
                init {
                    require(fromRequest)
                }
            }

            /**
             * Case when the access was granted when the Web App was launched from a link
             */
            data class WebApp(
                val webAppName: String
            ) : WriteAccessAllowed

            /**
             * Case when the access was granted when the bot was added to the attachment or side menu
             */
            data class FromAttachmentMenu(
                val fromAttachmentMenu: Boolean = true
            ) : WriteAccessAllowed {
                init {
                    require(fromAttachmentMenu)
                }
            }
        }

        /**
         * Telegram [ProximityAlertTriggered](https://core.telegram.org/bots/api#proximityalerttriggered) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class ProximityAlertTriggered(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val traveler: User,
            val watcher: User,
            val distance: Int
        )

        /**
         * Service messages related to [ski.gagar.vertigram.types.ForumTopic]
         */
        object ForumTopic {
            /**
             * Telegram [ForumTopicClosed](https://core.telegram.org/bots/api#forumtopicclosed) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object Closed

            /**
             * Telegram [ForumTopicCreated](https://core.telegram.org/bots/api#forumtopiccreated) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class Created(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val name: String,
                val iconColor: RgbColor,
                val iconCustomEmojiId: String? = null
            )

            /**
             * Telegram [ForumTopicEdited](https://core.telegram.org/bots/api#forumtopicedited) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class Edited(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val name: String? = null,
                val iconCustomEmojiId: String? = null
            )

            /**
             * Telegram [ForumTopicReopened](https://core.telegram.org/bots/api#forumtopicreopened) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object Reopened

            /**
             * Telegram [GeneralForumTopicHidden](https://core.telegram.org/bots/api#generalforumtopichidden) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object GeneralHidden

            /**
             * Telegram [GeneralForumTopicUnhidden](https://core.telegram.org/bots/api#generalforumtopicunhidden) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object GeneralUnhidden
        }

        /**
         * Service messages related to [ski.gagar.vertigram.types.Giveaway]
         */
        object Giveaway {
            /**
             * Telegram [GiveawayCompleted](https://core.telegram.org/bots/api#giveawaycompleted) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class Completed(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val winnerCount: Int,
                val unclaimedPrizeCount: Int? = null,
                val giveawayMessage: Message? = null
            )

            /**
             * Telegram [GiveawayCreated](https://core.telegram.org/bots/api#giveawaycreated) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object Created
        }

        /**
         * Service messages related to video chats.
         */
        object VideoChat {
            /**
             * Telegram [VideoChatScheduled](https://core.telegram.org/bots/api#videochatscheduled) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class Scheduled(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val startDate: Instant
            )

            /**
             * Telegram [VideoChatStarted](https://core.telegram.org/bots/api#videochatstarted) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data object Started

            /**
             * Telegram [VideoChatEnded](https://core.telegram.org/bots/api#videochatended) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class Ended(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val duration: Duration
            )

            /**
             * Telegram [VideoChatParticipantsInvited](https://core.telegram.org/bots/api#videochatparticipantsinvited) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            data class ParticipantsInvited(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val users: List<User>
            )
        }

        /**
         * Telegram [WebAppData](https://core.telegram.org/bots/api#webappdata) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class WebAppData(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val data: String,
            val buttonText: String
        )

    }

    /**
     * Telegram [MessageId](https://core.telegram.org/bots/api#messageid) type.
     *
     * For up-to-date documentation please consult the official Telegram docs.
     */
    data class Id(
        @JsonIgnore
        private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
        val messageId: Long
    )

}
