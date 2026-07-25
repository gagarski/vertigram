package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

class UpdateList(val list: List<Update<*>>)
class ParsedUpdateList(val delegate: List<Update.Parsed<*>>)

/**
 * Represents an incoming update.
 *
 * Each subtype represents the single update field that is set. Types used only as an update field value are nested
 * under the corresponding subtype as `Payload`.
 *
 * See Telegram's [Update](https://core.telegram.org/bots/api#update) documentation.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = Update.Message::class),
    JsonSubTypes.Type(value = Update.EditedMessage::class),
    JsonSubTypes.Type(value = Update.ChannelPost::class),
    JsonSubTypes.Type(value = Update.EditedChannelPost::class),
    JsonSubTypes.Type(value = Update.BusinessConnection::class),
    JsonSubTypes.Type(value = Update.BusinessMessage::class),
    JsonSubTypes.Type(value = Update.EditedBusinessMessage::class),
    JsonSubTypes.Type(value = Update.DeletedBusinessMessages::class),
    JsonSubTypes.Type(value = Update.GuestMessage::class),
    JsonSubTypes.Type(value = Update.MessageReaction::class),
    JsonSubTypes.Type(value = Update.MessageReactionCount::class),
    JsonSubTypes.Type(value = Update.InlineQuery::class),
    JsonSubTypes.Type(value = Update.ChosenInlineResult::class),
    JsonSubTypes.Type(value = Update.CallbackQuery::class),
    JsonSubTypes.Type(value = Update.ShippingQuery::class),
    JsonSubTypes.Type(value = Update.PreCheckoutQuery::class),
    JsonSubTypes.Type(value = Update.PurchasedPaidMedia::class),
    JsonSubTypes.Type(value = Update.Poll::class),
    JsonSubTypes.Type(value = Update.PollAnswer::class),
    JsonSubTypes.Type(value = Update.ManagedBot::class),
    JsonSubTypes.Type(value = Update.Subscription::class),
    JsonSubTypes.Type(value = Update.MyChatMember::class),
    JsonSubTypes.Type(value = Update.ChatMember::class),
    JsonSubTypes.Type(value = Update.ChatJoinRequest::class),
    JsonSubTypes.Type(value = Update.ChatBoost::class),
    JsonSubTypes.Type(value = Update.RemovedChatBoost::class),
//    This is INTENTIONALLY excluded from type deduction
//    JsonSubTypes.Type(value = Update2.Malformed::class),
)
sealed interface Update<T> {
    /** Unique identifier of the update. */
    val updateId: Long

    @get:JsonIgnore
    val date: Instant?

    /**
     * An accessor for the value of the field that has been set. Not serialized to JSON
     */
    @get:JsonIgnore
    val payload: T

    sealed interface Parsed<T> : Update<T>

    /** Case when the update contains a new incoming message. */
    @TelegramCodegen.Type
    data class Message internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming message. */
        val message: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = message
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new version of an edited message. */
    @TelegramCodegen.Type
    data class EditedMessage internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New version of the edited message. */
        val editedMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedMessage
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new incoming channel post. */
    @TelegramCodegen.Type
    data class ChannelPost internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming channel post. */
        val channelPost: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = channelPost
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new version of an edited channel post. */
    @TelegramCodegen.Type
    data class EditedChannelPost internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New version of the edited channel post. */
        val editedChannelPost: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedChannelPost
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new or changed business connection. */
    @TelegramCodegen.Type
    data class BusinessConnection internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New or changed business connection. */
        val businessConnection: ski.gagar.vertigram.telegram.types.BusinessConnection
    ) : Parsed<ski.gagar.vertigram.telegram.types.BusinessConnection> {
        override val payload: ski.gagar.vertigram.telegram.types.BusinessConnection = businessConnection
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new message from a connected business account. */
    @TelegramCodegen.Type
    data class BusinessMessage internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New message from a connected business account. */
        val businessMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = businessMessage
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a new version of a message from a connected business account. */
    @TelegramCodegen.Type
    data class EditedBusinessMessage internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New version of the message from a connected business account. */
        val editedBusinessMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedBusinessMessage
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains messages deleted from a connected business account. */
    @TelegramCodegen.Type
    data class DeletedBusinessMessages internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the deleted business messages. */
        val deletedBusinessMessages: Payload
    ) : Parsed<DeletedBusinessMessages.Payload> {
        override val payload: Payload = deletedBusinessMessages
        override val date: Instant? = null

        /**
         * Describes messages deleted from a connected business account.
         *
         * See Telegram's [BusinessMessagesDeleted](https://core.telegram.org/bots/api#businessmessagesdeleted)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique identifier of the business connection. */
            val businessConnectionId: String,
            /** Chat the messages were deleted from. */
            val chat: Chat,
            /** Identifiers of the deleted messages. */
            val messageIds: List<Long>
        ) {
            companion object
        }

        companion object
    }

    /** Case when the update contains a new message sent by a guest bot. */
    @TelegramCodegen.Type
    data class GuestMessage internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New message sent by a guest bot. */
        val guestMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = guestMessage
        override val date: Instant = payload.date

        companion object
    }

    /** Case when the update contains a reaction change made by a user. */
    @TelegramCodegen.Type
    data class MessageReaction internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the reaction change. */
        val messageReaction: Payload
    ) : Parsed<MessageReaction.Payload> {
        override val payload: Payload = messageReaction
        override val date: Instant = payload.date

        /**
         * Represents a change of a reaction on a message performed by a user.
         *
         * See Telegram's [MessageReactionUpdated](https://core.telegram.org/bots/api#messagereactionupdated)
         * documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat containing the message. */
            val chat: Chat,
            /** Unique identifier of the message inside the chat. */
            val messageId: Long,
            /** User that changed the reaction. */
            val user: User? = null,
            /** Chat on behalf of which the reaction was changed. */
            val actorChat: Chat? = null,
            /** Date of the change. */
            val date: Instant,
            /** Previous list of reaction types set by the user. */
            val oldReaction: List<Reaction>,
            /** New list of reaction types set by the user. */
            val newReaction: List<Reaction>
        ) {
            companion object
        }
        companion object
    }

    /** Case when the update contains changed anonymous reaction counts. */
    @TelegramCodegen.Type
    data class MessageReactionCount internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the changed reaction counts. */
        val messageReactionCount: Payload
    ) : Parsed<MessageReactionCount.Payload> {
        override val payload: Payload = messageReactionCount
        override val date: Instant = payload.date

        /**
         * Represents reaction changes on a message with anonymous reactions.
         *
         * See Telegram's
         * [MessageReactionCountUpdated](https://core.telegram.org/bots/api#messagereactioncountupdated) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat containing the message. */
            val chat: Chat,
            /** Unique identifier of the message inside the chat. */
            val messageId: Long,
            /** Date of the change. */
            val date: Instant,
            /** List of reactions present on the message. */
            val reactions: List<ReactionCount>
        ) {
            /**
             * Represents one reaction and its count.
             *
             * See Telegram's [ReactionCount](https://core.telegram.org/bots/api#reactioncount) documentation.
             */
            @TelegramCodegen.Type
            data class ReactionCount internal constructor(
                /** Type of the reaction. */
                val type: Reaction,
                /** Number of times the reaction was added. */
                val totalCount: Int
            ) {
                companion object
            }
            companion object
        }

        companion object
    }

    /** Case when the update contains a new incoming inline query. */
    @TelegramCodegen.Type
    data class InlineQuery internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming inline query. */
        val inlineQuery: ski.gagar.vertigram.telegram.types.InlineQuery
    ) : Parsed<ski.gagar.vertigram.telegram.types.InlineQuery> {
        override val payload: ski.gagar.vertigram.telegram.types.InlineQuery = inlineQuery
        override val date: Instant? = null

        companion object
    }

    /** Case when the update contains an inline-query result chosen by a user. */
    @TelegramCodegen.Type
    data class ChosenInlineResult internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the chosen result. */
        val chosenInlineResult: Payload
    ) : Parsed<ChosenInlineResult.Payload> {
        override val payload: Payload = chosenInlineResult
        override val date: Instant? = null

        /**
         * Represents an inline-query result chosen by a user and sent to their chat partner.
         *
         * See Telegram's [ChosenInlineResult](https://core.telegram.org/bots/api#choseninlineresult) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique identifier of the chosen result. */
            val resultId: String,
            /** User that chose the result. */
            val from: User,
            /** Sender location, for bots that require user location. */
            val location: Location? = null,
            /** Identifier of the sent inline message. */
            val inlineMessageId: String? = null,
            /** Query used to obtain the result. */
            val query: String
        ) {
            companion object
        }

        companion object
    }

    /** Case when the update contains a new incoming callback query. */
    @TelegramCodegen.Type
    data class CallbackQuery internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming callback query. */
        val callbackQuery: Payload
    ) : Parsed<CallbackQuery.Payload> {
        override val payload: Payload = callbackQuery
        override val date: Instant? = null

        /**
         * Represents an incoming callback query from a callback button.
         *
         * See Telegram's [CallbackQuery](https://core.telegram.org/bots/api#callbackquery) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique identifier for this query. */
            val id: String,
            /** Sender. */
            val from: User,
            /** Message sent by the bot with the callback button. */
            val message: ski.gagar.vertigram.telegram.types.Message? = null,
            /** Identifier of the message sent through the bot in inline mode. */
            val inlineMessageId: String? = null,
            /** Global identifier of the chat containing the callback button. */
            val chatInstance: String,
            /** Data associated with the callback button. */
            val data: String? = null,
            /** Short name of the game to be returned. */
            val gameShortName: String? = null
        ) {
            companion object
        }

        companion object
    }

    /** Case when the update contains a new incoming shipping query. */
    @TelegramCodegen.Type
    data class ShippingQuery internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming shipping query. */
        val shippingQuery: Payload
    ) : Parsed<ShippingQuery.Payload> {
        override val payload: Payload = shippingQuery
        override val date: Instant? = null

        /**
         * Contains information about an incoming shipping query.
         *
         * See Telegram's [ShippingQuery](https://core.telegram.org/bots/api#shippingquery) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique query identifier. */
            val id: String,
            /** User that sent the query. */
            val from: User,
            /** Bot-specified invoice payload. */
            val invoicePayload: String,
            /** User-specified shipping address. */
            val shippingAddress: ShippingAddress
        ) {
            companion object
        }

        companion object
    }

    /** Case when the update contains a new incoming pre-checkout query with full checkout information. */
    @TelegramCodegen.Type
    data class PreCheckoutQuery internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New incoming pre-checkout query with full checkout information. */
        val preCheckoutQuery: Payload
    ) : Parsed<PreCheckoutQuery.Payload> {
        override val payload: Payload = preCheckoutQuery
        override val date: Instant? = null

        /**
         * Contains information about an incoming pre-checkout query.
         *
         * See Telegram's [PreCheckoutQuery](https://core.telegram.org/bots/api#precheckoutquery) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique query identifier. */
            val id: String,
            /** User who sent the query. */
            val from: User,
            /** Three-letter ISO 4217 currency code, or `XTR` for payments in Telegram Stars. */
            val currency: String,
            /** Total price in the smallest units of the currency. */
            val totalAmount: Int,
            /** Bot-specified invoice payload. */
            val invoicePayload: String,
            /** Identifier of the shipping option chosen by the user. */
            val shippingOptionId: String? = null,
            /** Order information provided by the user. */
            val orderInfo: OrderInfo? = null
        ) {
            companion object
        }

        companion object
    }

    /** Case when a user purchased paid media with a non-empty payload sent by the bot in a non-channel chat. */
    @TelegramCodegen.Type
    data class PurchasedPaidMedia internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the paid media purchase. */
        val purchasedPaidMedia: Payload
    ) : Parsed<PurchasedPaidMedia.Payload> {
        override val payload: Payload = purchasedPaidMedia
        override val date: Instant? = null

        /**
         * Contains information about a paid media purchase.
         *
         * See Telegram's [PaidMediaPurchased](https://core.telegram.org/bots/api#paidmediapurchased) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Unique identifier of the update. */
            val id: String,
            /** User who purchased the media. */
            val from: User,
            /** Bot-specified paid media payload. */
            val paidMediaPayload: String,
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when the update contains a new poll state.
     *
     * Bots receive only updates about manually stopped polls and polls sent by the bot.
     */
    @TelegramCodegen.Type
    data class Poll internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** New poll state. */
        val poll: ski.gagar.vertigram.telegram.types.Poll
    ) : Parsed<ski.gagar.vertigram.telegram.types.Poll> {
        override val payload: ski.gagar.vertigram.telegram.types.Poll = poll
        override val date: Instant? = null

        companion object
    }

    /**
     * Case when a user changed their answer in a non-anonymous poll.
     *
     * Bots receive new votes only in polls sent by the bot.
     */
    @TelegramCodegen.Type
    data class PollAnswer internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Answer changed by the user. */
        val pollAnswer: ski.gagar.vertigram.telegram.types.Poll.Answer
    ) : Parsed<ski.gagar.vertigram.telegram.types.Poll.Answer> {
        override val payload: ski.gagar.vertigram.telegram.types.Poll.Answer = pollAnswer
        override val date: Instant? = null

        companion object
    }

    /** Case when a managed bot was created, or the token or owner of a managed bot was changed. */
    @TelegramCodegen.Type
    data class ManagedBot internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the managed bot update. */
        val managedBot: Payload
    ) : Parsed<ManagedBot.Payload> {
        override val payload: Payload = managedBot
        override val date: Instant? = null

        /**
         * Contains information about the creation, token update, or owner update of a bot managed by the current bot.
         *
         * See Telegram's [ManagedBotUpdated](https://core.telegram.org/bots/api#managedbotupdated) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** User that created the bot. */
            val user: User,
            /**
             * Information about the bot.
             *
             * The bot's token can be fetched using
             * [getManagedBotToken][ski.gagar.vertigram.telegram.methods.getManagedBotToken].
             */
            val bot: User
        ) {
            companion object
        }

        companion object
    }

    /** Case when a user payment subscription has changed. */
    @TelegramCodegen.Type
    data class Subscription internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the changed user payment subscription. */
        val subscription: Payload
    ) : Parsed<Subscription.Payload> {
        override val payload: Payload = subscription
        override val date: Instant? = null

        /**
         * Contains information about changes to a user payment subscription toward the current bot.
         *
         * See Telegram's
         * [BotSubscriptionUpdated](https://core.telegram.org/bots/api#botsubscriptionupdated) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** User who subscribed for payments toward the bot. */
            val user: User,
            /** Bot-specified invoice payload. */
            val invoicePayload: String,
            /** New state of the subscription. */
            val state: State
        ) {
            enum class State {
                /** Case when the user canceled the subscription. */
                @JsonProperty("canceled") CANCELED,
                /** Case when the user re-enabled a previously canceled subscription. */
                @JsonProperty("active") ACTIVE,
                /** Case when payment for the subscription failed. */
                @JsonProperty("failed") FAILED
            }
            companion object
        }

        companion object
    }

    /**
     * Case when the bot's chat member status was updated in a chat.
     *
     * For private chats, this update is received only when the bot is blocked or unblocked by the user.
     */
    @TelegramCodegen.Type
    data class MyChatMember internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the bot's updated chat member status. */
        val myChatMember: ChatMember.Payload
    ) : Parsed<ChatMember.Payload> {
        override val payload: ChatMember.Payload = myChatMember
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when a chat member's status was updated in a chat.
     *
     * The bot must be an administrator in the chat and explicitly request `chat_member` updates.
     */
    @TelegramCodegen.Type
    data class ChatMember internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the updated chat member status. */
        val chatMember: Payload
    ) : Parsed<ChatMember.Payload> {
        override val payload: Payload = chatMember
        override val date: Instant = payload.date

        /**
         * Represents changes in the status of a chat member.
         *
         * See Telegram's [ChatMemberUpdated](https://core.telegram.org/bots/api#chatmemberupdated) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat the user belongs to. */
            val chat: Chat,
            /** Performer of the action that resulted in the change. */
            val from: User,
            /** Date the change was made. */
            val date: Instant,
            /** Previous information about the chat member. */
            val oldChatMember: ski.gagar.vertigram.telegram.types.ChatMember,
            /** New information about the chat member. */
            val newChatMember: ski.gagar.vertigram.telegram.types.ChatMember,
            /** Chat invite link used by the user to join the chat; for joining by invite link events only. */
            val inviteLink: ChatInviteLink? = null,
            /**
             * Whether the user joined the chat after sending a direct join request without using an invite link and
             * being approved by an administrator.
             */
            val viaJoinRequest: Boolean = false,
            /** Whether the user joined the chat via a chat folder invite link. */
            val viaChatFolderInviteLink: Boolean = false
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when a request to join the chat was sent.
     *
     * The bot must have the `canInviteUsers` administrator right in the chat to receive these updates.
     */
    @TelegramCodegen.Type
    data class ChatJoinRequest internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Request to join the chat. */
        val chatJoinRequest: Payload
    ) : Parsed<ChatJoinRequest.Payload> {
        override val payload: Payload = chatJoinRequest
        override val date: Instant = payload.date

        /**
         * Represents a join request sent to a chat.
         *
         * See Telegram's [ChatJoinRequest](https://core.telegram.org/bots/api#chatjoinrequest) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat to which the request was sent. */
            val chat: Chat,
            /** User who sent the join request. */
            val from: User,
            /**
             * Identifier of a private chat with the user who sent the join request.
             *
             * The bot can use this identifier for 5 minutes to send messages until the join request is processed.
             */
            val userChatId: Long,
            /** Date the request was sent. */
            val date: Instant,
            /**
             * Identifier of the join request query; for bots assigned to process join requests only.
             *
             * When present, the bot must call
             * [sendChatJoinRequestWebApp][ski.gagar.vertigram.telegram.methods.sendChatJoinRequestWebApp] or
             * [answerChatJoinRequestQuery][ski.gagar.vertigram.telegram.methods.answerChatJoinRequestQuery] within
             * 10 seconds.
             */
            val queryId: String? = null,
            /** Bio of the user. */
            val bio: String? = null,
            /** Chat invite link used by the user to send the join request. */
            val inviteLink: ChatInviteLink? = null
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when a chat boost was added or changed.
     *
     * The bot must be an administrator in the chat to receive these updates.
     */
    @TelegramCodegen.Type
    data class ChatBoost internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the added or changed chat boost. */
        val chatBoost: Payload
    ) : Parsed<ChatBoost.Payload> {
        override val payload: Payload = chatBoost
        override val date: Instant = payload.boost.addDate

        /**
         * Represents a boost added to a chat or changed.
         *
         * See Telegram's [ChatBoostUpdated](https://core.telegram.org/bots/api#chatboostupdated) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat which was boosted. */
            val chat: Chat,
            /** Information about the chat boost. */
            val boost: ski.gagar.vertigram.telegram.types.ChatBoost
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when a boost was removed from a chat.
     *
     * The bot must be an administrator in the chat to receive these updates.
     */
    @TelegramCodegen.Type
    data class RemovedChatBoost internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Information about the removed chat boost. */
        val chatBoostRemoved: Payload
    ) : Parsed<RemovedChatBoost.Payload> {
        override val payload: Payload = chatBoostRemoved
        override val date: Instant = payload.removeDate

        /**
         * Represents a boost removed from a chat.
         *
         * See Telegram's [ChatBoostRemoved](https://core.telegram.org/bots/api#chatboostremoved) documentation.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            /** Chat which was boosted. */
            val chat: Chat,
            /** Unique identifier of the boost. */
            val boostId: String,
            /** Point in time when the boost was removed. */
            val removeDate: Instant,
            /** Source of the removed boost. */
            val source: ski.gagar.vertigram.telegram.types.ChatBoost.Source
        ) {
            companion object
        }

        companion object
    }

    /** Case when an incoming update could not be parsed into one of the supported update types. */
    @TelegramCodegen.Type
    data class Malformed internal constructor(
        /** Unique identifier of the update. */
        override val updateId: Long,
        /** Raw data of the malformed update. */
        val malformedRawData: Map<String, Any?>
    ) : Update<Map<String, Any?>> {
        override val payload: Map<String, Any?> = malformedRawData
        override val date: Instant? = null

        companion object
    }

    /**
     * Update type
     */
    enum class Type {
        @JsonProperty("message")
        MESSAGE,
        @JsonProperty("edited_message")
        EDITED_MESSAGE,
        @JsonProperty("channel_post")
        CHANNEL_POST,
        @JsonProperty("edited_channel_post")
        EDITED_CHANNEL_POST,
        @JsonProperty("business_connection")
        BUSINESS_CONNECTION,
        @JsonProperty("business_message")
        BUSINESS_MESSAGE,
        @JsonProperty("edited_business_message")
        EDITED_BUSINESS_MESSAGE,
        @JsonProperty("deleted_business_messages")
        DELETED_BUSINESS_MESSAGES,
        @JsonProperty("guest_message")
        GUEST_MESSAGE,
        @JsonProperty("message_reaction")
        MESSAGE_REACTION,
        @JsonProperty("message_reaction_count")
        MESSAGE_REACTION_COUNT,
        @JsonProperty("inline_query")
        INLINE_QUERY,
        @JsonProperty("chosen_inline_result")
        CHOSEN_INLINE_RESULT,
        @JsonProperty("callback_query")
        CALLBACK_QUERY,
        @JsonProperty("shipping_query")
        SHIPPING_QUERY,
        @JsonProperty("pre_checkout_query")
        PRE_CHECKOUT_QUERY,
        @JsonProperty("purchased_paid_media")
        PURCHASED_PAID_MEDIA,
        @JsonProperty("poll")
        POLL,
        @JsonProperty("poll_answer")
        POLL_ANSWER,
        @JsonProperty("managed_bot")
        MANAGED_BOT,
        @JsonProperty("subscription")
        SUBSCRIPTION,
        @JsonProperty("my_chat_member")
        MY_CHAT_MEMBER,
        @JsonProperty("chat_member")
        CHAT_MEMBER,
        @JsonProperty("chat_join_request")
        CHAT_JOIN_REQUEST,
        @JsonProperty("chat_boost")
        CHAT_BOOST,
        @JsonProperty("removed_chat_boost")
        REMOVED_CHAT_BOOST
    }
}
