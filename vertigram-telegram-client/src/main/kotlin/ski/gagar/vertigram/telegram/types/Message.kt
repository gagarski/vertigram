package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalCaptionWithEntities
import ski.gagar.vertigram.telegram.types.formattedtext.HasOptionalTextWithEntities
import ski.gagar.vertigram.telegram.types.richmessage.RichMessage
import java.time.Duration
import java.time.Instant

/**
 * Represents a message.
 *
 * See Telegram's [Message](https://core.telegram.org/bots/api#message) documentation.
 */
@TelegramCodegen.Type
data class Message internal constructor(
    /** Unique message identifier inside this chat. */
    val messageId: Long,
    /** Unique identifier of a message thread to which the message belongs. */
    val messageThreadId: Long? = null,
    /** Sender of the message; empty for messages sent to channels. */
    val from: User? = null,
    /** Sender of the message when sent on behalf of a chat. */
    val senderChat: Chat? = null,
    /** Number of boosts added by the sender of the message. */
    val senderBoostCount: Int? = null,
    /** Bot that actually sent the message on behalf of the business account. */
    val senderBusinessBot: User? = null,
    /** Tag of the message sender. */
    val senderTag: String? = null,
    /** Date the message was sent. */
    val date: Instant,
    /** Unique identifier of the guest query that was answered by the message. */
    val guestQueryId: String? = null,
    /** User for whom an ephemeral message was sent. */
    val receiverUser: User? = null,
    /** Unique identifier of the ephemeral message. */
    val ephemeralMessageId: Long? = null,
    /** Unique identifier of the business connection from which the message was received. */
    val businessConnectionId: String? = null,
    /** Chat the message belongs to. */
    val chat: Chat,
    /** Information about the original message for forwarded messages. */
    val forwardOrigin: Origin? = null,
    /** Whether the message was sent to a forum topic. */
    @get:JvmName("getIsTopicMessage")
    val isTopicMessage: Boolean = false,
    /** Whether the message is a channel post automatically forwarded to a connected discussion group. */
    @get:JvmName("getIsAutomaticForward")
    val isAutomaticForward: Boolean = false,
    /** Original message for replies in the same chat and message thread. */
    val replyToMessage: Message? = null,
    /** Information about the message being replied to from another chat or forum topic. */
    val externalReply: ExternalReplyInfo? = null,
    /** Quoted part of the replied-to message. */
    val quote: TextQuote? = null,
    /** Story being replied to. */
    val replyToStory: Story? = null,
    /** Bot through which the message was sent. */
    val viaBot: User? = null,
    /** User that called the guest bot that sent the message. */
    val guestBotCallerUser: User? = null,
    /** Chat that called the guest bot that sent the message. */
    val guestBotCallerChat: Chat? = null,
    /** Date the message was last edited. */
    val editDate: Instant? = null,
    /** Whether the message can't be forwarded. */
    val hasProtectedContent: Boolean = false,
    /** Whether the message was sent by an implicit action while the user was offline. */
    @get:JvmName("getIsFromOffline")
    val isFromOffline: Boolean = false,
    /** Unique identifier of the media message group to which the message belongs. */
    val mediaGroupId: String? = null,
    /** Signature of the post author or custom title of an anonymous group administrator. */
    val authorSignature: String? = null,
    /** Number of Telegram Stars paid by the sender to send the message. */
    val paidStarCount: Int? = null,
    /** Identifier of the checklist task that is being replied to. */
    val replyToChecklistTaskId: Int? = null,
    /** Identifier of the poll option that is being replied to. */
    val replyToPollOptionId: String? = null,
    /** Information about the direct messages chat topic that contains the message. */
    val directMessagesTopic: DirectMessagesTopic? = null,
    /** Whether the channel post is a paid post. */
    @get:JvmName("getIsPaidPost")
    val isPaidPost: Boolean = false,
    /** Text of the message. */
    override val text: String? = null,
    /** Special entities that appear in [text]. */
    override val entities: List<MessageEntity>? = null,
    /** Options used for link preview generation for the message. */
    val linkPreviewOptions: LinkPreviewOptions? = null,
    /** Unique identifier of the message effect added to the message. */
    val effectId: String? = null,
    /** Rich message content. */
    val richMessage: RichMessage? = null,
    /** Message is an animation, information about the animation. */
    val animation: Animation? = null,
    /** Message is an audio file, information about the file. */
    val audio: Audio? = null,
    /** Message is a general file, information about the file. */
    val document: Document? = null,
    /** Message is a live photo, information about the live photo. */
    val livePhoto: LivePhoto? = null,
    /** Message contains paid media, information about the paid media. */
    val paidMedia: PaidMediaInfo? = null,
    /** Message is a photo, available sizes of the photo. */
    val photo: List<PhotoSize>? = null,
    /** Message is a sticker, information about the sticker. */
    val sticker: Sticker? = null,
    /** Message is a forwarded story. */
    val story: Story? = null,
    /** Message is a video, information about the video. */
    val video: Video? = null,
    /** Message is a video note, information about the video message. */
    val videoNote: VideoNote? = null,
    /** Message is a voice message, information about the file. */
    val voice: Voice? = null,
    /** Caption for the animation, audio, document, paid media, photo, video, or voice. */
    override val caption: String? = null,
    /** Special entities that appear in [caption]. */
    override val captionEntities: List<MessageEntity>? = null,
    /** Whether the caption must be shown above the message media. */
    val showCaptionAboveMedia: Boolean = false,
    /** Whether the message media is covered by a spoiler animation. */
    val hasMediaSpoiler: Boolean = false,
    /** Message is a checklist. */
    val checklist: Checklist? = null,
    /** Message is a shared contact, information about the contact. */
    val contact: Contact? = null,
    /** Message is a dice with a random value. */
    val dice: Dice? = null,
    /** Message is a game, information about the game. */
    val game: Game? = null,
    /** Message is a native poll, information about the poll. */
    val poll: Poll? = null,
    /** Message is a venue, information about the venue. */
    val venue: Venue? = null,
    /** Message is a shared location, information about the location. */
    val location: Location? = null,
    /** New members added to the group or supergroup. */
    val newChatMembers: List<User>? = null,
    /** Member removed from the group, or the bot itself for a member that left. */
    val leftChatMember: User? = null,
    /** Service message: a chat owner left the chat. */
    val chatOwnerLeft: Service.ChatOwner.Left? = null,
    /** Service message: a chat owner changed. */
    val chatOwnerChanged: Service.ChatOwner.Changed? = null,
    /** New chat title. */
    val newChatTitle: String? = null,
    /** New chat photo. */
    val newChatPhoto: List<PhotoSize>? = null,
    /** Service message: the chat photo was deleted. */
    val deleteChatPhoto: Boolean = false,
    /** Service message: the group was created. */
    val groupChatCreated: Boolean = false,
    /** Service message: the supergroup was created. */
    val supergroupChatCreated: Boolean = false,
    /** Service message: the channel was created. */
    val channelChatCreated: Boolean = false,
    /** Service message: the auto-delete timer settings changed. */
    val messageAutoDeleteTimerChanged: Service.MessageAutoDeleteTimerChanged? = null,
    /** Identifier of the supergroup to which the group was migrated. */
    val migrateToChatId: Long? = null,
    /** Identifier of the group from which the supergroup was migrated. */
    val migrateFromChatId: Long? = null,
    /** Message pinned by the service message. */
    val pinnedMessage: Message? = null,
    /** Message is an invoice for a payment. */
    val invoice: Invoice? = null,
    /** Service message: a payment was successful. */
    val successfulPayment: Service.SuccessfulPayment? = null,
    /** Service message: a payment was refunded. */
    val refundedPayment: Service.RefundedPayment? = null,
    /** Service message: users were shared with the bot. */
    val usersShared: Service.UsersShared? = null,
    /** Service message: a chat was shared with the bot. */
    val chatShared: Service.ChatShared? = null,
    /** Service message: a managed bot was created. */
    val managedBotCreated: Service.ManagedBotCreated? = null,
    /** Service message: a regular gift was sent or received. */
    val gift: Service.GiftInfo? = null,
    /** Service message: a unique gift was sent or received. */
    val uniqueGift: Service.UniqueGiftInfo? = null,
    /** Service message: a regular gift was upgraded to a unique gift. */
    val giftUpgradeSent: Service.GiftInfo? = null,
    /** Domain name of a website on which the user logged in. */
    val connectedWebsite: String? = null,
    /**
     * Service message: the user allowed the bot to write messages after adding it to the attachment menu or a Web App.
     */
    val writeAccessAllowed: Service.WriteAccessAllowed? = null,
    /** Telegram Passport data. */
    val passportData: Passport.Data? = null,
    /** Service message: a proximity alert was triggered. */
    val proximityAlertTriggered: Service.ProximityAlertTriggered? = null,
    /** Service message: a user boosted the chat. */
    val boostAdded: ChatBoost.Added? = null,
    /** Service message: the chat background changed. */
    val chatBackgroundSet: ChatBackground? = null,
    /** Service message: checklist tasks were marked done or not done. */
    val checklistTasksDone: Service.ChecklistTasks.Done? = null,
    /** Service message: tasks were added to a checklist. */
    val checklistTasksAdded: Service.ChecklistTasks.Added? = null,
    /** Service message: the chat was added to a community. */
    val communityChatAdded: Service.CommunityChatAdded? = null,
    /** Service message: the chat was removed from a community. */
    val communityChatRemoved: Service.CommunityChatRemoved? = null,
    /** Service message: the price for paid messages in the direct messages chat changed. */
    val directMessagePriceChanged: Service.DirectMessagePriceChanged? = null,
    /** Information about a suggested post. */
    val suggestedPostInfo: SuggestedPost.Info? = null,
    /** Service message: a suggested post was approved. */
    val suggestedPostApproved: Service.SuggestedPost.Approved? = null,
    /** Service message: approval of a suggested post failed. */
    val suggestedPostApprovalFailed: Service.SuggestedPost.ApprovalFailed? = null,
    /** Service message: a suggested post was declined. */
    val suggestedPostDeclined: Service.SuggestedPost.Declined? = null,
    /** Service message: payment for a suggested post was received. */
    val suggestedPostPaid: Service.SuggestedPost.Paid? = null,
    /** Service message: payment for a suggested post was refunded. */
    val suggestedPostRefunded: Service.SuggestedPost.Refunded? = null,
    /** Service message: an option was added to a poll. */
    val pollOptionAdded: Service.PollOption.Added? = null,
    /** Service message: an option was deleted from a poll. */
    val pollOptionDeleted: Service.PollOption.Deleted? = null,
    /** Service message: a forum topic was created. */
    val forumTopicCreated: Service.ForumTopic.Created? = null,
    /** Service message: a forum topic was edited. */
    val forumTopicEdited: Service.ForumTopic.Edited? = null,
    /** Service message: a forum topic was closed. */
    val forumTopicClosed: Service.ForumTopic.Closed? = null,
    /** Service message: a forum topic was reopened. */
    val forumTopicReopened: Service.ForumTopic.Reopened? = null,
    /** Service message: the General forum topic was hidden. */
    val generalForumTopicHidden: Service.ForumTopic.GeneralHidden? = null,
    /** Service message: the General forum topic was unhidden. */
    val generalForumTopicUnhidden: Service.ForumTopic.GeneralUnhidden? = null,
    /** Service message: a scheduled giveaway was created. */
    val giveawayCreated: Service.Giveaway.Created? = null,
    /** Message is a scheduled giveaway. */
    val giveaway: Giveaway? = null,
    /** Message is a giveaway with public winners. */
    val giveawayWinners: Giveaway.Winners? = null,
    /** Service message: a giveaway was completed. */
    val giveawayCompleted: Service.Giveaway.Completed? = null,
    /** Service message: the price for paid messages in the chat changed. */
    val paidMessagePriceChanged: Service.PaidMessagePriceChanged? = null,
    /** Service message: a video chat was scheduled. */
    val videoChatScheduled: Service.VideoChat.Scheduled? = null,
    /** Service message: a video chat started. */
    val videoChatStarted: Service.VideoChat.Started? = null,
    /** Service message: a video chat ended. */
    val videoChatEnded: Service.VideoChat.Ended? = null,
    /** Service message: users were invited to a video chat. */
    val videoChatParticipantsInvited: Service.VideoChat.ParticipantsInvited? = null,
    /** Service message: data sent by a Web App. */
    val webAppData: Service.WebAppData? = null,
    /** Inline keyboard attached to the message. */
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
) : HasOptionalTextWithEntities, HasOptionalCaptionWithEntities {
    /**
     * Contains information about a message that is being replied to from another chat or forum topic.
     *
     * See Telegram's [ExternalReplyInfo](https://core.telegram.org/bots/api#externalreplyinfo) documentation.
     */
    @TelegramCodegen.Type
    data class ExternalReplyInfo internal constructor(
        /** Origin of the message being replied to. */
        val origin: Message.Origin,
        /** Chat the original message belongs to. */
        val chat: Chat? = null,
        /** Unique message identifier inside the original chat. */
        val messageId: Long? = null,
        /** Link preview generation options for the original message. */
        val linkPreviewOptions: LinkPreviewOptions? = null,
        /** Original message contains an animation. */
        val animation: Animation? = null,
        /** Original message contains an audio file. */
        val audio: Audio? = null,
        /** Original message contains a document. */
        val document: Document? = null,
        /** Original message contains a live photo. */
        val livePhoto: LivePhoto? = null,
        /** Original message contains paid media. */
        val paidMedia: PaidMediaInfo? = null,
        /** Original message contains a photo. */
        val photo: List<PhotoSize>? = null,
        /** Original message contains a sticker. */
        val sticker: Sticker? = null,
        /** Original message contains a story. */
        val story: Story? = null,
        /** Original message contains a video. */
        val video: Video? = null,
        /** Original message contains a video note. */
        val videoNote: VideoNote? = null,
        /** Original message contains a voice message. */
        val voice: Voice? = null,
        /** Whether the original message media is covered by a spoiler animation. */
        val hasMediaSpoiler: Boolean = false,
        /** Original message contains a checklist. */
        val checklist: Checklist? = null,
        /** Original message contains a shared contact. */
        val contact: Contact? = null,
        /** Original message contains a dice. */
        val dice: Dice? = null,
        /** Original message contains a game. */
        val game: Game? = null,
        /** Original message contains a giveaway. */
        val giveaway: Giveaway? = null,
        /** Original message contains giveaway winners. */
        val giveawayWinners: Giveaway.Winners? = null,
        /** Original message contains an invoice. */
        val invoice: Invoice? = null,
        /** Original message contains a shared location. */
        val location: Location? = null,
        /** Original message contains a poll. */
        val poll: Poll? = null,
        /** Original message contains a venue. */
        val venue: Venue? = null
    ) {
        companion object
    }

    /**
     * Describes paid media added to a message.
     *
     * See Telegram's [PaidMediaInfo](https://core.telegram.org/bots/api#paidmediainfo) documentation.
     */
    @TelegramCodegen.Type
    data class PaidMediaInfo internal constructor(
        /** Number of Telegram Stars paid for the media. */
        val starCount: Int,
        /** Information about the paid media. */
        val paidMedia: List<PaidMedia>
    ) {
        companion object
    }

    /**
     * Describes the origin of a message.
     *
     * See Telegram's [MessageOrigin](https://core.telegram.org/bots/api#messageorigin) documentation.
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
         * Case when the message was originally sent by a known user.
         *
         * See Telegram's [MessageOriginUser](https://core.telegram.org/bots/api#messageoriginuser) documentation.
         */
        @TelegramCodegen.Type
        data class User internal constructor(
            /** Date when the message was sent originally. */
            val date: Instant,
            /** User that sent the message originally. */
            val senderUser: ski.gagar.vertigram.telegram.types.User
        ) : Origin {
            override val type: Type = Type.USER

            companion object
        }

        /**
         * Case when the message was originally sent by an unknown user.
         *
         * See Telegram's [MessageOriginHiddenUser](https://core.telegram.org/bots/api#messageoriginhiddenuser)
         * documentation.
         */
        @TelegramCodegen.Type
        data class HiddenUser internal constructor(
            /** Date when the message was sent originally. */
            val date: Instant,
            /** Name of the user that sent the message originally. */
            val senderUserName: String
        ) : Origin {
            override val type: Type = Type.HIDDEN_USER

            companion object
        }

        /**
         * Case when the message was originally sent on behalf of a chat.
         *
         * See Telegram's [MessageOriginChat](https://core.telegram.org/bots/api#messageoriginchat) documentation.
         */
        @TelegramCodegen.Type
        data class Chat internal constructor(
            /** Date when the message was sent originally. */
            val date: Instant,
            /** Chat that sent the message originally. */
            val senderChat: ski.gagar.vertigram.telegram.types.Chat,
            /** Signature of the original post author. */
            val authorSignature: String? = null
        ) : Origin {
            override val type: Type = Type.CHAT

            companion object
        }

        /**
         * Case when the message was originally a channel post.
         *
         * See Telegram's [MessageOriginChannel](https://core.telegram.org/bots/api#messageoriginchannel) documentation.
         */
        @TelegramCodegen.Type
        data class Channel internal constructor(
            /** Date when the message was sent originally. */
            val date: Instant,
            /** Channel chat to which the message was originally sent. */
            val chat: ski.gagar.vertigram.telegram.types.Chat,
            /** Unique message identifier inside the channel. */
            val messageId: Long,
            /** Signature of the original post author. */
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
     * Contains information about the quoted part of a message that is replied to.
     *
     * See Telegram's [TextQuote](https://core.telegram.org/bots/api#textquote) documentation.
     */
    @TelegramCodegen.Type
    data class TextQuote internal constructor(
        /** Text of the quoted part of a message. */
        override val text: String,
        /** Approximate quote position in the original message in UTF-16 code units. */
        val position: Int,
        /** Special entities that appear in [text]. */
        override val entities: List<MessageEntity>? = null,
        /** Whether the quote was chosen manually by the message sender. */
        @get:JvmName("getIsManual")
        val isManual: Boolean = false
    ) : HasOptionalTextWithEntities {
        companion object
    }

    /**
     * Describes options used for link preview generation.
     *
     * See Telegram's [LinkPreviewOptions](https://core.telegram.org/bots/api#linkpreviewoptions) documentation.
     */
    @TelegramCodegen.Type
    data class LinkPreviewOptions internal constructor(
        /** Whether the link preview is disabled. */
        @get:JvmName("getIsDisabled")
        val isDisabled: Boolean = false,
        /** URL to use for link preview generation. */
        val url: String? = null,
        /** Whether the media in the link preview should be shrunk. */
        val preferSmallMedia: Boolean = false,
        /** Whether the media in the link preview should be enlarged. */
        val preferLargeMedia: Boolean = false,
        /** Whether the link preview must be shown above the message text. */
        val showAboveText: Boolean = false
    ) {
        companion object
    }

    /**
     * Contains types, which act only as fields of [Message] and represent service messages according to docs.
     */
    object Service {
        /**
         * Describes a service message about a change in the auto-delete timer settings.
         *
         * See Telegram's
         * [MessageAutoDeleteTimerChanged](https://core.telegram.org/bots/api#messageautodeletetimerchanged)
         * documentation.
         */
        @TelegramCodegen.Type
        data class MessageAutoDeleteTimerChanged internal constructor(
            /** New auto-delete time for messages in the chat. */
            val messageAutoDeleteTime: Duration
        ) {
            companion object
        }

        /**
         * Service messages related to chat owner changes.
         */
        object ChatOwner {
            /**
             * Describes a service message about the owner leaving a chat.
             *
             * See Telegram's [ChatOwnerLeft](https://core.telegram.org/bots/api#chatownerleft) documentation.
             */
            @TelegramCodegen.Type
            data class Left internal constructor(
                /** New owner of the chat. */
                val newOwner: User? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about the owner of a chat changing.
             *
             * See Telegram's [ChatOwnerChanged](https://core.telegram.org/bots/api#chatownerchanged) documentation.
             */
            @TelegramCodegen.Type
            data class Changed internal constructor(
                /** New owner of the chat. */
                val newOwner: User
            ) {
                companion object
            }
        }

        /**
         * Contains basic information about a successful payment.
         *
         * See Telegram's [SuccessfulPayment](https://core.telegram.org/bots/api#successfulpayment) documentation.
         */
        @TelegramCodegen.Type
        data class SuccessfulPayment internal constructor(
            /** Three-letter ISO 4217 currency code, or `XTR` for Telegram Stars. */
            val currency: String,
            /** Total price in the smallest units of [currency]. */
            val totalAmount: Int,
            /** Bot-specified invoice payload. */
            val invoicePayload: String,
            /** Expiration date of the subscription. */
            val subscriptionExpirationDate: Instant? = null,
            /** Whether the payment is a recurring subscription payment. */
            @get:JvmName("getIsRecurring")
            val isRecurring: Boolean = false,
            /** Whether the payment is the first payment of a recurring subscription. */
            @get:JvmName("getIsFirstRecurring")
            val isFirstRecurring: Boolean = false,
            /** Identifier of the chosen shipping option. */
            val shippingOptionId: String? = null,
            /** Order information provided by the user. */
            val orderInfo: OrderInfo? = null,
            /** Telegram payment identifier. */
            val telegramPaymentChargeId: String,
            /** Payment provider identifier. */
            val providerPaymentChargeId: String
        ) {
            companion object
        }

        /**
         * Contains basic information about a refunded payment.
         *
         * See Telegram's [RefundedPayment](https://core.telegram.org/bots/api#refundedpayment) documentation.
         */
        @TelegramCodegen.Type
        data class RefundedPayment internal constructor(
            /** Three-letter ISO 4217 currency code, or `XTR` for Telegram Stars. */
            val currency: String,
            /** Total refunded amount in the smallest units of [currency]. */
            val totalAmount: Int,
            /** Bot-specified invoice payload. */
            val invoicePayload: String,
            /** Telegram payment identifier. */
            val telegramPaymentChargeId: String,
            /** Payment provider identifier. */
            val providerPaymentChargeId: String? = null
        ) {
            companion object
        }

        /**
         * Contains information about users shared with the bot.
         *
         * See Telegram's [UsersShared](https://core.telegram.org/bots/api#usersshared) documentation.
         */
        @TelegramCodegen.Type
        data class UsersShared internal constructor(
            /** Identifier of the request. */
            val requestId: Long,
            /** Information about the shared users. */
            val users: List<SharedUser>
        ) {
            /**
             * Contains information about a user shared with the bot.
             *
             * See Telegram's [SharedUser](https://core.telegram.org/bots/api#shareduser) documentation.
             */
            @TelegramCodegen.Type
            data class SharedUser internal constructor(
                /** Identifier of the shared user. */
                val userId: Long,
                /** First name of the shared user. */
                val firstName: String? = null,
                /** Last name of the shared user. */
                val lastName: String? = null,
                /** Username of the shared user. */
                val username: String? = null,
                /** Available sizes of the shared user's profile photo. */
                val photo: List<PhotoSize>? = null
            ) {
                companion object
            }

            companion object
        }

        /**
         * Contains information about a chat shared with the bot.
         *
         * See Telegram's [ChatShared](https://core.telegram.org/bots/api#chatshared) documentation.
         */
        @TelegramCodegen.Type
        data class ChatShared internal constructor(
            /** Identifier of the request. */
            val requestId: Long,
            /** Identifier of the shared chat. */
            val chatId: Long,
            /** Title of the shared chat. */
            val title: String? = null,
            /** Username of the shared chat. */
            val username: String? = null,
            /** Available sizes of the shared chat photo. */
            val photo: List<PhotoSize>? = null
        ) {
            companion object
        }

        /**
         * Describes a service message about the creation of a managed bot.
         *
         * See Telegram's [ManagedBotCreated](https://core.telegram.org/bots/api#managedbotcreated) documentation.
         */
        @TelegramCodegen.Type
        data class ManagedBotCreated internal constructor(
            /** Managed bot that was created. */
            val bot: User
        ) {
            companion object
        }

        /**
         * Describes a regular gift that was sent or received.
         *
         * See Telegram's [GiftInfo](https://core.telegram.org/bots/api#giftinfo) documentation.
         */
        @TelegramCodegen.Type
        data class GiftInfo internal constructor(
            /** Information about the gift. */
            val gift: Gift,
            /** Unique identifier of the received gift for the business account. */
            val ownedGiftId: String? = null,
            /** Number of Telegram Stars that can be claimed instead of the gift. */
            val convertStarCount: Int? = null,
            /** Number of Telegram Stars prepaid for the gift upgrade. */
            val prepaidUpgradeStarCount: Int? = null,
            /** Whether the gift can be upgraded to a unique gift. */
            val canBeUpgraded: Boolean = false,
            /** Whether the gift's upgrade was paid separately from the gift purchase. */
            @get:JvmName("getIsUpgradeSeparate")
            val isUpgradeSeparate: Boolean = false,
            /** Number of the upgraded gift. */
            val uniqueGiftNumber: Int? = null,
            /** Text added to the gift. */
            val text: String? = null,
            /** Special entities that appear in [text]. */
            val entities: List<MessageEntity>? = null,
            /** Whether the sender and gift text are shown only to the gift receiver. */
            @get:JvmName("getIsPrivate")
            val isPrivate: Boolean = false
        ) {
            companion object
        }

        /**
         * Describes a unique gift that was sent or received.
         *
         * See Telegram's [UniqueGiftInfo](https://core.telegram.org/bots/api#uniquegiftinfo) documentation.
         */
        @TelegramCodegen.Type
        data class UniqueGiftInfo internal constructor(
            /** Information about the gift. */
            val gift: UniqueGift,
            /** Origin of the gift. */
            val origin: Origin,
            /** Currency used for the last resale of the gift. */
            val lastResaleCurrency: String? = null,
            /** Amount paid for the last resale of the gift. */
            val lastResaleAmount: Long? = null,
            /** Unique identifier of the received gift for the business account. */
            val ownedGiftId: String? = null,
            /** Number of Telegram Stars required to transfer the gift. */
            val transferStarCount: Int? = null,
            /** Earliest date when the gift can be transferred. */
            val nextTransferDate: Instant? = null,
        ) {
            enum class Origin {
                /** Gift was upgraded from a regular gift. */
                @JsonProperty("upgrade")
                UPGRADE,
                /** Gift was transferred from another owner. */
                @JsonProperty("transfer")
                TRANSFER,
                /** Gift was bought from another owner. */
                @JsonProperty("resale")
                RESALE,
                /** Gift was received after another user paid for its upgrade. */
                @JsonProperty("gifted_upgrade")
                GIFTED_UPGRADE,
                /** Gift was received through an offer. */
                @JsonProperty("offer")
                OFFER;

                companion object {
                    const val UPGRADE_STR = "upgrade"
                    const val TRANSFER_STR = "transfer"
                    const val RESALE_STR = "resale"
                    const val GIFTED_UPGRADE_STR = "gifted_upgrade"
                    const val OFFER_STR = "offer"
                }
            }
            companion object
        }

        /**
         * Describes a service message about a user allowing the bot to write messages after adding it to the attachment
         * menu, launching a Web App, or accepting an explicit request.
         *
         * See Telegram's [WriteAccessAllowed](https://core.telegram.org/bots/api#writeaccessallowed) documentation.
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
        @JsonSubTypes(
            JsonSubTypes.Type(WriteAccessAllowed.FromRequest::class),
            JsonSubTypes.Type(WriteAccessAllowed.WebApp::class),
            JsonSubTypes.Type(WriteAccessAllowed.FromAttachmentMenu::class),
        )
        interface WriteAccessAllowed {
            /**
             * Case when the access was granted after the user accepted an explicit request from a Web App sent by the
             * method `requestWriteAccess`.
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
                /** Name of the Web App from which access was granted. */
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
         * Describes a service message about a proximity alert triggered while sharing a live location.
         *
         * See Telegram's [ProximityAlertTriggered](https://core.telegram.org/bots/api#proximityalerttriggered)
         * documentation.
         */
        @TelegramCodegen.Type
        data class ProximityAlertTriggered internal constructor(
            /** User that triggered the alert. */
            val traveler: User,
            /** User that set the alert. */
            val watcher: User,
            /** Distance between the users in meters. */
            val distance: Int
        ) {
            companion object
        }

        object ChecklistTasks {
            /**
             * Describes a service message about checklist tasks marked as done or not done.
             *
             * See Telegram's [ChecklistTasksDone](https://core.telegram.org/bots/api#checklisttasksdone) documentation.
             */
            @TelegramCodegen.Type
            data class Done internal constructor(
                /** Message containing the checklist whose tasks were updated. */
                val checklistMessage: Message,
                /** Identifiers of tasks marked as done. */
                val markedAsDoneTaskIds: List<Int>? = null,
                /** Identifiers of tasks marked as not done. */
                val markedAsNotDoneTaskIds: List<Int>? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about tasks added to a checklist.
             *
             * See Telegram's [added checklist tasks](https://core.telegram.org/bots/api#checklisttasksadded)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Added internal constructor(
                /** Message containing the checklist to which tasks were added. */
                val checklistMessage: Message,
                /** Tasks that were added. */
                val tasks: List<Checklist.Task>? = null
            ) {
                companion object
            }

        }

        /**
         * Describes a service message about a chat being added to a community.
         *
         * See Telegram's [CommunityChatAdded](https://core.telegram.org/bots/api#communitychatadded) documentation.
         */
        @TelegramCodegen.Type
        data class CommunityChatAdded internal constructor(
            /** Community to which the chat was added. */
            val community: Community
        ) { companion object }

        /**
         * Describes a service message about a chat being removed from a community.
         *
         * See Telegram's [CommunityChatRemoved](https://core.telegram.org/bots/api#communitychatremoved) documentation.
         */
        data object CommunityChatRemoved

        /**
         * Describes a service message about a change in the price for paid messages in a direct messages chat.
         *
         * See Telegram's [DirectMessagePriceChanged](https://core.telegram.org/bots/api#directmessagepricechanged)
         * documentation.
         */
        @TelegramCodegen.Type
        data class DirectMessagePriceChanged internal constructor(
            /** Whether direct messages are enabled for the channel chat. */
            val areDirectMessagesEnabled: Boolean,
            /** New number of Telegram Stars required to send a direct message. */
            val directMessageStarCount: Int? = null
        ) {
            companion object
        }

        /**
         * Service messages related to suggested posts.
         */
        object SuggestedPost {
            /**
             * Describes a service message about the approval of a suggested post.
             *
             * See Telegram's [SuggestedPostApproved](https://core.telegram.org/bots/api#suggestedpostapproved)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Approved internal constructor(
                /** Message containing the suggested post. */
                val suggestedPostMessage: Message? = null,
                /** Amount to be paid for the post. */
                val price: SuggestedPost.Price? = null,
                /** Date when the post will be published. */
                val sendDate: Instant
            ) {
                companion object
            }

            /**
             * Describes a service message about failed approval of a suggested post.
             *
             * See Telegram's
             * [SuggestedPostApprovalFailed](https://core.telegram.org/bots/api#suggestedpostapprovalfailed)
             * documentation.
             */
            @TelegramCodegen.Type
            data class ApprovalFailed internal constructor(
                /** Message containing the suggested post. */
                val suggestedPostMessage: Message? = null,
                /** Amount that was to be paid for the post. */
                val price: SuggestedPost.Price
            ) {
                companion object
            }

            /**
             * Describes a service message about the rejection of a suggested post.
             *
             * See Telegram's [SuggestedPostDeclined](https://core.telegram.org/bots/api#suggestedpostdeclined)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Declined internal constructor(
                /** Message containing the suggested post. */
                val suggestedPostMessage: Message? = null,
                /** Comment explaining why the post was declined. */
                val comment: String? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about a successful payment for a suggested post.
             *
             * See Telegram's [SuggestedPostPaid](https://core.telegram.org/bots/api#suggestedpostpaid) documentation.
             */
            @TelegramCodegen.Type
            data class Paid internal constructor(
                /** Message containing the suggested post. */
                val suggestedPostMessage: Message? = null,
                /** Currency in which the payment was made. */
                val currency: String,
                /** Amount paid in the smallest units of [currency]. */
                val amount: Long? = null,
                /** Amount paid in Telegram Stars. */
                val starAmount: StarAmount? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about a refunded payment for a suggested post.
             *
             * See Telegram's [SuggestedPostRefunded](https://core.telegram.org/bots/api#suggestedpostrefunded)
             * documentation.
             */
            @TelegramCodegen.Type
            data class Refunded internal constructor(
                /** Message containing the suggested post. */
                val suggestedPostMessage: Message? = null,
                /** Reason for the refund. */
                val reason: Reason
            ) {
                enum class Reason {
                    /** Post was deleted within 24 hours of being posted. */
                    @JsonProperty(POST_DELETED_STR)
                    POST_DELETED,
                    /** Payment was refunded independently of the post. */
                    @JsonProperty(PAYMENT_REFUNDED_STR)
                    PAYMENT_REFUNDED;

                    companion object {
                        const val POST_DELETED_STR = "post_deleted"
                        const val PAYMENT_REFUNDED_STR = "payment_refunded"
                    }
                }

                companion object
            }
        }

        /**
         * Service messages related to poll options.
         */
        object PollOption {
            /**
             * Describes a service message about an option added to a poll.
             *
             * See Telegram's [PollOptionAdded](https://core.telegram.org/bots/api#polloptionadded) documentation.
             */
            @TelegramCodegen.Type
            data class Added internal constructor(
                /** Message containing the poll. */
                val pollMessage: Message? = null,
                /** Persistent identifier of the added option. */
                val optionPersistentId: String,
                /** Text of the added option. */
                val optionText: String,
                /** Special entities that appear in [optionText]. */
                val optionTextEntities: List<MessageEntity>? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about an option deleted from a poll.
             *
             * See Telegram's [PollOptionDeleted](https://core.telegram.org/bots/api#polloptiondeleted) documentation.
             */
            @TelegramCodegen.Type
            data class Deleted internal constructor(
                /** Message containing the poll. */
                val pollMessage: Message? = null,
                /** Persistent identifier of the deleted option. */
                val optionPersistentId: String,
                /** Text of the deleted option. */
                val optionText: String,
                /** Special entities that appear in [optionText]. */
                val optionTextEntities: List<MessageEntity>? = null
            ) {
                companion object
            }
        }


        /**
         * Service messages related to [ski.gagar.vertigram.telegram.types.ForumTopic]
         */
        object ForumTopic {
            /**
             * Describes a service message about a forum topic being closed.
             *
             * See Telegram's [ForumTopicClosed](https://core.telegram.org/bots/api#forumtopicclosed) documentation.
             */
            data object Closed

            /**
             * Describes a service message about a new forum topic being created.
             *
             * See Telegram's [ForumTopicCreated](https://core.telegram.org/bots/api#forumtopiccreated) documentation.
             */
            @TelegramCodegen.Type
            data class Created internal constructor(
                /** Name of the topic. */
                val name: String,
                /** Color of the topic icon. */
                val iconColor: RgbColor,
                /** Unique identifier of the custom emoji shown as the topic icon. */
                val iconCustomEmojiId: String? = null,
                /** Whether the topic name was created implicitly by the Telegram client. */
                @get:JvmName("getIsNameImplicit")
                val isNameImplicit: Boolean = false
            ) {
                companion object
            }

            /**
             * Describes a service message about a forum topic being edited.
             *
             * See Telegram's [ForumTopicEdited](https://core.telegram.org/bots/api#forumtopicedited) documentation.
             */
            @TelegramCodegen.Type
            data class Edited internal constructor(
                /** New name of the topic. */
                val name: String? = null,
                /** New unique identifier of the custom emoji shown as the topic icon. */
                val iconCustomEmojiId: String? = null
            ) {
                companion object
            }

            /**
             * Describes a service message about a forum topic being reopened.
             *
             * See Telegram's [ForumTopicReopened](https://core.telegram.org/bots/api#forumtopicreopened) documentation.
             */
            data object Reopened

            /**
             * Describes a service message about the General forum topic being hidden.
             *
             * See Telegram's
             * [GeneralForumTopicHidden](https://core.telegram.org/bots/api#generalforumtopichidden) documentation.
             */
            data object GeneralHidden

            /**
             * Describes a service message about the General forum topic being unhidden.
             *
             * See Telegram's
             * [GeneralForumTopicUnhidden](https://core.telegram.org/bots/api#generalforumtopicunhidden) documentation.
             */
            data object GeneralUnhidden
        }

        /**
         * Service messages related to [ski.gagar.vertigram.telegram.types.Giveaway]
         */
        object Giveaway {
            /**
             * Describes a service message about a giveaway being completed.
             *
             * See Telegram's [GiveawayCompleted](https://core.telegram.org/bots/api#giveawaycompleted) documentation.
             */
            @TelegramCodegen.Type
            data class Completed internal constructor(
                /** Number of winners in the giveaway. */
                val winnerCount: Int,
                /** Number of undistributed prizes. */
                val unclaimedPrizeCount: Int? = null,
                /** Message with the giveaway that was completed. */
                val giveawayMessage: Message? = null,
                /** Whether the giveaway is a Telegram Star giveaway. */
                @get:JvmName("getIsStarGiveaway")
                val isStarGiveaway: Boolean = false
            ) {
                companion object
            }

            /**
             * Describes a service message about a scheduled giveaway being created.
             *
             * See Telegram's [GiveawayCreated](https://core.telegram.org/bots/api#giveawaycreated) documentation.
             */
            @TelegramCodegen.Type
            data class Created internal constructor(
                /** Number of Telegram Stars to be split between giveaway winners. */
                val prizeStarCount: Int? = null
            ) {
                companion object
            }
        }

        /**
         * Describes a service message about a change in the price for paid messages in a chat.
         *
         * See Telegram's [PaidMessagePriceChanged](https://core.telegram.org/bots/api#paidmessagepricechanged)
         * documentation.
         */
        @TelegramCodegen.Type
        data class PaidMessagePriceChanged internal constructor(
            /** New number of Telegram Stars required to send a message in the chat. */
            val paidMessageStarCount: Int
        ) {
            companion object
        }

        /**
         * Service messages related to video chats.
         */
        object VideoChat {
            /**
             * Describes a service message about a video chat scheduled in the chat.
             *
             * See Telegram's [VideoChatScheduled](https://core.telegram.org/bots/api#videochatscheduled) documentation.
             */
            @TelegramCodegen.Type
            data class Scheduled internal constructor(
                /** Point in time when the video chat is scheduled to start. */
                val startDate: Instant
            ) {
                companion object
            }

            /**
             * Describes a service message about a video chat starting in the chat.
             *
             * See Telegram's [VideoChatStarted](https://core.telegram.org/bots/api#videochatstarted) documentation.
             */
            data object Started

            /**
             * Describes a service message about a video chat ending in the chat.
             *
             * See Telegram's [VideoChatEnded](https://core.telegram.org/bots/api#videochatended) documentation.
             */
            @TelegramCodegen.Type
            data class Ended internal constructor(
                /** Duration of the video chat. */
                val duration: Duration
            ) {
                companion object
            }

            /**
             * Describes a service message about users being invited to a video chat.
             *
             * See Telegram's
             * [VideoChatParticipantsInvited](https://core.telegram.org/bots/api#videochatparticipantsinvited)
             * documentation.
             */
            @TelegramCodegen.Type
            data class ParticipantsInvited internal constructor(
                /** Users invited to the video chat. */
                val users: List<User>
            ) {
                companion object
            }
        }

        /**
         * Describes data sent from a Web App to the bot.
         *
         * See Telegram's [WebAppData](https://core.telegram.org/bots/api#webappdata) documentation.
         */
        @TelegramCodegen.Type
        data class WebAppData internal constructor(
            /** Data sent by the Web App. */
            val data: String,
            /** Text of the keyboard button from which the Web App was opened. */
            val buttonText: String
        ) {
            companion object
        }

    }

    /**
     * Represents a unique message identifier.
     *
     * See Telegram's [MessageId](https://core.telegram.org/bots/api#messageid) documentation.
     */
    @TelegramCodegen.Type
    data class Id internal constructor(
        /** Unique message identifier. */
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
