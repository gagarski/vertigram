package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalCaptionWithEntities
import ski.gagar.vertigram.telegram.types.richtext.HasOptionalTextWithEntities
import java.time.Duration
import java.time.Instant

/**
 * Telegram [Message](https://core.telegram.org/bots/api#message) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type(wrapRichText = false)
data class Message internal constructor(
    val messageId: Long,
    val messageThreadId: Long? = null,
    val from: User? = null,
    val senderChat: Chat? = null,
    val senderBoostCount: Int? = null,
    val senderBusinessBot: User? = null,
    val date: Instant,
    val businessConnectionId: String? = null,
    val chat: Chat,
    val forwardOrigin: Origin? = null,
    @get:JvmName("getIsTopicMessage")
    val isTopicMessage: Boolean = false,
    @get:JvmName("getIsAutomaticForward")
    val isAutomaticForward: Boolean = false,
    val replyToMessage: Message? = null,
    val externalReply: ExternalReplyInfo? = null,
    val quote: TextQuote? = null,
    val replyToStory: Story? = null,
    val viaBot: User? = null,
    val editDate: Instant? = null,
    val hasProtectedContent: Boolean = false,
    @get:JvmName("getIsFromOffline")
    val isFromOffline: Boolean = false,
    val mediaGroupId: String? = null,
    val authorSignature: String? = null,
    val paidStarCount: Int? = null,
    override val text: String? = null,
    override val entities: List<MessageEntity>? = null,
    val linkPreviewOptions: LinkPreviewOptions? = null,
    val effectId: String? = null,
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    val paidMedia: PaidMediaInfo? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val story: Story? = null,
    val video: Video? = null,
    val videoNote: VideoNote? = null,
    val voice: Voice? = null,
    override val caption: String? = null,
    override val captionEntities: List<MessageEntity>? = null,
    val showCaptionAboveMedia: Boolean = false,
    val hasMediaSpoiler: Boolean = false,
    val checklist: Checklist? = null,
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
    val refundedPayment: Service.RefundedPayment? = null,
    val usersShared: Service.UsersShared? = null,
    val chatShared: Service.ChatShared? = null,
    val gift: Service.GiftInfo? = null,
    val uniqueGift: Service.UniqueGiftInfo? = null,
    val connectedWebsite: String? = null,
    val writeAccessAllowed: Service.WriteAccessAllowed? = null,
    val passportData: Passport.Data? = null,
    val proximityAlertTriggered: Service.ProximityAlertTriggered? = null,
    val boostAdded: ChatBoost.Added? = null,
    val chatBackgroundSet: ChatBackground? = null,
    val checklistTasksDone: Service.ChecklistTasks.Done? = null,
    val checklistTasksAdded: Service.ChecklistTasks.Added? = null,
    val directMessagePriceChanged: Service.DirectMessagePriceChanged? = null,
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
    val paidMessagePriceChanged: Service.PaidMessagePriceChanged? = null,
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
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class ExternalReplyInfo internal constructor(
        val origin: Message.Origin,
        val chat: Chat? = null,
        val messageId: Long? = null,
        val linkPreviewOptions: LinkPreviewOptions? = null,
        val animation: Animation? = null,
        val audio: Audio? = null,
        val document: Document? = null,
        val paidMedia: PaidMediaInfo? = null,
        val photo: List<PhotoSize>? = null,
        val sticker: Sticker? = null,
        val story: Story? = null,
        val video: Video? = null,
        val videoNote: VideoNote? = null,
        val voice: Voice? = null,
        val hasMediaSpoiler: Boolean = false,
        val checklist: Checklist? = null,
        val contact: Contact? = null,
        val dice: Dice? = null,
        val game: Game? = null,
        val giveaway: Giveaway? = null,
        val giveawayWinners: Giveaway.Winners? = null,
        val invoice: Invoice? = null,
        val location: Location? = null,
        val poll: Poll? = null,
        val venue: Venue? = null
    ) {
        companion object
    }

    /**
     * Telegram [PaidMediaInfo](https://core.telegram.org/bots/api#paidmediainfo) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class PaidMediaInfo internal constructor(
        val starCount: Int,
        val paidMedia: List<PaidMedia>
    ) {
        companion object
    }

    /**
     * Telegram [MessageOrigin](https://core.telegram.org/bots/api#messageorigin) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
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
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class User internal constructor(
            val date: Instant,
            val senderUser: ski.gagar.vertigram.telegram.types.User
        ) : Origin {
            override val type: Type = Type.USER

            companion object
        }

        /**
         * Telegram [MessageOriginHiddenUser](https://core.telegram.org/bots/api#messageoriginhiddenuser) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class HiddenUser internal constructor(
            val date: Instant,
            val senderUserName: String
        ) : Origin {
            override val type: Type = Type.HIDDEN_USER

            companion object
        }

        /**
         * Telegram [MessageOriginChat](https://core.telegram.org/bots/api#messageoriginchat) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Chat internal constructor(
            val date: Instant,
            val senderChat: ski.gagar.vertigram.telegram.types.Chat,
            val authorSignature: String? = null
        ) : Origin {
            override val type: Type = Type.CHAT

            companion object
        }

        /**
         * Telegram [MessageOriginChannel](https://core.telegram.org/bots/api#messageoriginchannel) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Channel internal constructor(
            val date: Instant,
            val chat: ski.gagar.vertigram.telegram.types.Chat,
            val messageId: Long,
            val authorSignature: String? = null
        ) : Origin {
            override val type: Type = Type.CHANNEL

            companion object
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
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type(wrapRichText = false)
    data class TextQuote internal constructor(
        override val text: String,
        val position: Int,
        override val entities: List<MessageEntity>? = null,
        @get:JvmName("getIsManual")
        val isManual: Boolean = false
    ) : HasOptionalTextWithEntities {
        companion object
    }

    /**
     * Telegram [LinkPreviewOptions](https://core.telegram.org/bots/api#linkpreviewoptions) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class LinkPreviewOptions internal constructor(
        @get:JvmName("getIsDisabled")
        val isDisabled: Boolean = false,
        val url: String? = null,
        val preferSmallMedia: Boolean = false,
        val preferLargeMedia: Boolean = false,
        val showAboveText: Boolean = false
    ) {
        companion object
    }

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
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class MessageAutoDeleteTimerChanged internal constructor(
            val messageAutoDeleteTime: Duration
        ) {
            companion object
        }

        /**
         * Telegram [SuccessfulPayment](https://core.telegram.org/bots/api#successfulpayment) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class SuccessfulPayment internal constructor(
            val currency: String,
            val totalAmount: Int,
            val invoicePayload: String,
            val subscriptionExpirationDate: Instant? = null,
            @get:JvmName("getIsRecurring")
            val isRecurring: Boolean = false,
            @get:JvmName("getIsFirstRecurring")
            val isFirstRecurring: Boolean = false,
            val shippingOptionId: String? = null,
            val orderInfo: OrderInfo? = null,
            val telegramPaymentChargeId: String,
            val providerPaymentChargeId: String
        ) {
            companion object
        }

        /**
         * Telegram [RefundedPayment](https://core.telegram.org/bots/api#refundedpayment) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class RefundedPayment internal constructor(
            val currency: String,
            val totalAmount: Int,
            val invoicePayload: String,
            val telegramPaymentChargeId: String,
            val providerPaymentChargeId: String? = null
        ) {
            companion object
        }

        /**
         * Telegram [UsersShared](https://core.telegram.org/bots/api#usersshared) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class UsersShared internal constructor(
            val requestId: Long,
            val users: List<SharedUser>
        ) {
            /**
             * Telegram [SharedUser](https://core.telegram.org/bots/api#shareduser) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class SharedUser internal constructor(
                val userId: Long,
                val firstName: String? = null,
                val lastName: String? = null,
                val username: String? = null,
                val photo: List<PhotoSize>? = null
            ) {
                companion object
            }

            companion object
        }

        /**
         * Telegram [ChatShared](https://core.telegram.org/bots/api#chatshared) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type(wrapRichText = false)
        data class ChatShared internal constructor(
            val requestId: Long,
            val chatId: Long,
            val title: String? = null,
            val username: String? = null,
            val photo: List<PhotoSize>? = null
        ) {
            companion object
        }

        /**
         * Telegram [GiftInfo](https://core.telegram.org/bots/api#giftinfo) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type(wrapRichText = false)
        data class GiftInfo internal constructor(
            val gift: Gift,
            val ownedGiftId: String? = null,
            val convertStarCount: Int? = null,
            val prepaidUpgradeStarCount: Int? = null,
            val canBeUpgraded: Boolean = false,
            val text: String? = null,
            val entities: List<MessageEntity>? = null,
            @get:JvmName("getIsPrivate")
            val isPrivate: Boolean = false
        ) {
            companion object
        }

        /**
         * Telegram [UniqueGiftInfo](https://core.telegram.org/bots/api#uniquegiftinfo) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type(wrapRichText = false)
        data class UniqueGiftInfo internal constructor(
            val gift: UniqueGift,
            val origin: Origin,
            val lastResaleStarCount: Int? = null,
            val ownedGiftId: String? = null,
            val transferStarCount: Int? = null,
            val nextTransferDate: Instant? = null,
        ) {
            enum class Origin {
                @JsonProperty("upgrade")
                UPGRADE,
                @JsonProperty("transfer")
                TRANSFER,
                @JsonProperty("resale")
                RESALE;

                companion object {
                    const val UPGRADE_STR = "upgrade"
                    const val TRANSFER_STR = "transfer"
                    const val RESALE_STR = "resale"
                }
            }
            companion object
        }

        /**
         * Telegram [WriteAccessAllowed](https://core.telegram.org/bots/api#writeaccessallowed) type.
         *
         * Subtypes (which are nested) are synthetic subtypes representing cases when exactly one of the original type field
         * is present.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
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
            // @TelegramCodegen.Type // intentionally disabled
            data class FromRequest internal constructor(val fromRequest: Boolean = true) : WriteAccessAllowed {
                init {
                    require(fromRequest)
                }

                companion object {
                    fun create() = FromRequest()
                    operator fun invoke() = FromRequest()
                }
            }

            /**
             * Case when the access was granted when the Web App was launched from a link
             */
            @TelegramCodegen.Type
            data class WebApp internal constructor(
                val webAppName: String
            ) : WriteAccessAllowed {
                companion object
            }

            /**
             * Case when the access was granted when the bot was added to the attachment or side menu
             */
            // @TelegramCodegen.Type // intentionally disabled
            data class FromAttachmentMenu internal constructor(
                val fromAttachmentMenu: Boolean = true
            ) : WriteAccessAllowed {
                init {
                    require(fromAttachmentMenu)
                }

                companion object {
                    fun create() = FromRequest()
                    operator fun invoke() = FromRequest()
                }
            }
        }

        /**
         * Telegram [ProximityAlertTriggered](https://core.telegram.org/bots/api#proximityalerttriggered) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class ProximityAlertTriggered internal constructor(
            val traveler: User,
            val watcher: User,
            val distance: Int
        ) {
            companion object
        }

        object ChecklistTasks {
            /**
             * Telegram [ChecklistTasksDone](https://core.telegram.org/bots/api#checklisttasksdone) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Done internal constructor(
                val checklistMessage: Message,
                val markedAsDoneTaskIds: List<Int>? = null,
                val markedAsNotDoneTaskIds: List<Int>? = null
            ) {
                companion object
            }

            /**
             * Telegram [ChecklistTasksAdded](https://core.telegram.org/bots/api#checklisttasksadded) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Added internal constructor(
                val checklistMessage: Message,
                val tasks: List<Checklist.Task>? = null
            ) {
                companion object
            }

        }

        /**
         * Telegram [DirectMessagePriceChanged](https://core.telegram.org/bots/api#directmessagepricechanged) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class DirectMessagePriceChanged internal constructor(
            val areDirectMessagesEnabled: Boolean,
            val directMessageStarCount: Int
        ) {
            companion object
        }


        /**
         * Service messages related to [ski.gagar.vertigram.telegram.types.ForumTopic]
         */
        object ForumTopic {
            /**
             * Telegram [ForumTopicClosed](https://core.telegram.org/bots/api#forumtopicclosed) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            data object Closed

            /**
             * Telegram [ForumTopicCreated](https://core.telegram.org/bots/api#forumtopiccreated) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Created internal constructor(
                val name: String,
                val iconColor: RgbColor,
                val iconCustomEmojiId: String? = null
            ) {
                companion object
            }

            /**
             * Telegram [ForumTopicEdited](https://core.telegram.org/bots/api#forumtopicedited) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Edited internal constructor(
                val name: String? = null,
                val iconCustomEmojiId: String? = null
            ) {
                companion object
            }

            /**
             * Telegram [ForumTopicReopened](https://core.telegram.org/bots/api#forumtopicreopened) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            data object Reopened

            /**
             * Telegram [GeneralForumTopicHidden](https://core.telegram.org/bots/api#generalforumtopichidden) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            data object GeneralHidden

            /**
             * Telegram [GeneralForumTopicUnhidden](https://core.telegram.org/bots/api#generalforumtopicunhidden) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            data object GeneralUnhidden
        }

        /**
         * Service messages related to [ski.gagar.vertigram.telegram.types.Giveaway]
         */
        object Giveaway {
            /**
             * Telegram [GiveawayCompleted](https://core.telegram.org/bots/api#giveawaycompleted) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Completed internal constructor(
                val winnerCount: Int,
                val unclaimedPrizeCount: Int? = null,
                val giveawayMessage: Message? = null,
                @get:JvmName("getIsStarGiveaway")
                val isStarGiveaway: Boolean = false
            ) {
                companion object
            }

            /**
             * Telegram [GiveawayCreated](https://core.telegram.org/bots/api#giveawaycreated) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Created internal constructor(
                val prizeStarCount: Int? = null
            ) {
                companion object
            }
        }

        /**
         * Telegram [PaidMessagePriceChanged](https://core.telegram.org/bots/api#paidmessagepricechanged) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class PaidMessagePriceChanged internal constructor(
            val paidMessageStarCount: Int
        ) {
            companion object
        }

        /**
         * Service messages related to video chats.
         */
        object VideoChat {
            /**
             * Telegram [VideoChatScheduled](https://core.telegram.org/bots/api#videochatscheduled) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Scheduled internal constructor(
                val startDate: Instant
            ) {
                companion object
            }

            /**
             * Telegram [VideoChatStarted](https://core.telegram.org/bots/api#videochatstarted) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            data object Started

            /**
             * Telegram [VideoChatEnded](https://core.telegram.org/bots/api#videochatended) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class Ended internal constructor(
                val duration: Duration
            ) {
                companion object
            }

            /**
             * Telegram [VideoChatParticipantsInvited](https://core.telegram.org/bots/api#videochatparticipantsinvited) type.
             *
             * For up-to-date documentation, please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class ParticipantsInvited internal constructor(
                val users: List<User>
            ) {
                companion object
            }
        }

        /**
         * Telegram [WebAppData](https://core.telegram.org/bots/api#webappdata) type.
         *
         * For up-to-date documentation, please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class WebAppData internal constructor(
            val data: String,
            val buttonText: String
        ) {
            companion object
        }

    }

    /**
     * Telegram [MessageId](https://core.telegram.org/bots/api#messageid) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Id internal constructor(
        val messageId: Long
    ) {
        companion object
    }

    companion object
}

/**
 * Is [Message] forwarded?
 */
val Message.isForwarded
    get() = forwardOrigin != null