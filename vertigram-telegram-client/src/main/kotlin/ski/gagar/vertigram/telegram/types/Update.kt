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
 * Telegram [Update](https://core.telegram.org/bots/api#update) type.
 *
 * The original type is split into subtypes given "You __must__ use exactly one of the optional fields."
 * condition, each of the subclasses represent the case of respecting field being set.
 *
 * Some classes which represent only the value of an update field are nested into that [Update] subtype
 * under name `Payload`, though there are exceptions for some fundamental types such as
 * [ski.gagar.vertigram.telegram.types.InlineQuery] or [ski.gagar.vertigram.telegram.types.Message].
 *
 * For up-to-date documentation please consult the official Telegram docs.
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
    JsonSubTypes.Type(value = Update.MyChatMember::class),
    JsonSubTypes.Type(value = Update.ChatMember::class),
    JsonSubTypes.Type(value = Update.ChatJoinRequest::class),
    JsonSubTypes.Type(value = Update.ChatBoost::class),
    JsonSubTypes.Type(value = Update.RemovedChatBoost::class),
//    This is INTENTIONALLY excluded from type deduction
//    JsonSubTypes.Type(value = Update2.Malformed::class),
)
sealed interface Update<T> {
    val updateId: Long

    @get:JsonIgnore
    val date: Instant?

    /**
     * An accessor for the value of the field that has been set. Not serialized to JSON
     */
    @get:JsonIgnore
    val payload: T

    sealed interface Parsed<T> : Update<T>

    /**
     * Case when [message] is set
     */
    @TelegramCodegen.Type
    data class Message internal constructor(
        override val updateId: Long,
        val message: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = message
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [editedMessage] is set
     */
    @TelegramCodegen.Type
    data class EditedMessage internal constructor(
        override val updateId: Long,
        val editedMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedMessage
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [channelPost] is set
     */
    @TelegramCodegen.Type
    data class ChannelPost internal constructor(
        override val updateId: Long,
        val channelPost: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = channelPost
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [editedChannelPost] is set
     */
    @TelegramCodegen.Type
    data class EditedChannelPost internal constructor(
        override val updateId: Long,
        val editedChannelPost: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedChannelPost
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [businessConnection] is set
     */
    @TelegramCodegen.Type
    data class BusinessConnection internal constructor(
        override val updateId: Long,
        val businessConnection: ski.gagar.vertigram.telegram.types.BusinessConnection
    ) : Parsed<ski.gagar.vertigram.telegram.types.BusinessConnection> {
        override val payload: ski.gagar.vertigram.telegram.types.BusinessConnection = businessConnection
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [businessMessage] is set
     */
    @TelegramCodegen.Type
    data class BusinessMessage internal constructor(
        override val updateId: Long,
        val businessMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = businessMessage
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [editedBusinessMessage] is set
     */
    @TelegramCodegen.Type
    data class EditedBusinessMessage internal constructor(
        override val updateId: Long,
        val editedBusinessMessage: ski.gagar.vertigram.telegram.types.Message
    ) : Parsed<ski.gagar.vertigram.telegram.types.Message> {
        override val payload: ski.gagar.vertigram.telegram.types.Message = editedBusinessMessage
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [deletedBusinessMesages] is set
     */
    @TelegramCodegen.Type
    data class DeletedBusinessMessages internal constructor(
        override val updateId: Long,
        val deletedBusinessMessaged: Payload
    ) : Parsed<DeletedBusinessMessages.Payload> {
        override val payload: Payload = deletedBusinessMessaged
        override val date: Instant? = null

        /**
         * Telegram [BusinessMessagesDeleted](https://core.telegram.org/bots/api#businessmessagesdeleted) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val businessConnectionId: String,
            val chat: Chat,
            val messageId: List<Long>
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [messageReaction] is set
     */
    @TelegramCodegen.Type
    data class MessageReaction internal constructor(
        override val updateId: Long,
        val messageReaction: Payload
    ) : Parsed<MessageReaction.Payload> {
        override val payload: Payload = messageReaction
        override val date: Instant = payload.date

        /**
         * Telegram [MessageReactionUpdated](https://core.telegram.org/bots/api#messagereactionupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val messageId: Long,
            val user: User? = null,
            val actorChat: Chat? = null,
            val date: Instant,
            val oldReaction: List<Reaction>,
            val newReaction: List<Reaction>
        ) {
            companion object
        }
        companion object
    }

    /**
     * Case when [messageReactionCount] is set
     */
    @TelegramCodegen.Type
    data class MessageReactionCount internal constructor(
        override val updateId: Long,
        val messageReactionCount: Payload
    ) : Parsed<MessageReactionCount.Payload> {
        override val payload: Payload = messageReactionCount
        override val date: Instant = payload.date

        /**
         * Telegram [MessageReactionCountUpdated](https://core.telegram.org/bots/api#messagereactioncountupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val messageId: Long,
            val date: Instant,
            val reactions: List<ReactionCount>
        ) {
            /**
             * Telegram [ReactionCount](https://core.telegram.org/bots/api#reactioncount) type.
             *
             * For up-to-date documentation please consult the official Telegram docs.
             */
            @TelegramCodegen.Type
            data class ReactionCount internal constructor(
                val type: Reaction,
                val totalCount: Int
            ) {
                companion object
            }
            companion object
        }

        companion object
    }

    /**
     * Case when [inlineQuery] is set
     */
    @TelegramCodegen.Type
    data class InlineQuery internal constructor(
        override val updateId: Long,
        val inlineQuery: ski.gagar.vertigram.telegram.types.InlineQuery
    ) : Parsed<ski.gagar.vertigram.telegram.types.InlineQuery> {
        override val payload: ski.gagar.vertigram.telegram.types.InlineQuery = inlineQuery
        override val date: Instant? = null

        companion object
    }

    /**
     * Case when [ChosenInlineResult] is set
     */
    @TelegramCodegen.Type
    data class ChosenInlineResult internal constructor(
        override val updateId: Long,
        val chosenInlineResult: Payload
    ) : Parsed<ChosenInlineResult.Payload> {
        override val payload: Payload = chosenInlineResult
        override val date: Instant? = null

        /**
         * Telegram [ChosenInlineResult](https://core.telegram.org/bots/api#choseninlineresult) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val resultId: String,
            val from: User,
            val location: Location? = null,
            val inlineMessageId: String? = null,
            val query: String
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [callbackQuery] is set
     */
    @TelegramCodegen.Type
    data class CallbackQuery internal constructor(
        override val updateId: Long,
        val callbackQuery: Payload
    ) : Parsed<CallbackQuery.Payload> {
        override val payload: Payload = callbackQuery
        override val date: Instant? = null

        /**
         * Telegram [CallbackQuery](https://core.telegram.org/bots/api#callbackquery) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val id: String,
            val from: User,
            val message: ski.gagar.vertigram.telegram.types.Message? = null,
            val inlineMessageId: String? = null,
            val chatInstance: String,
            val data: String? = null,
            val gameShortName: String? = null
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [shippingQuery] is set
     */
    @TelegramCodegen.Type
    data class ShippingQuery internal constructor(
        override val updateId: Long,
        val shippingQuery: Payload
    ) : Parsed<ShippingQuery.Payload> {
        override val payload: Payload = shippingQuery
        override val date: Instant? = null

        /**
         * Telegram [ShippingQuery](https://core.telegram.org/bots/api#shippingquery) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val id: String,
            val from: User,
            val invoicePayload: String,
            val shippingAddress: ShippingAddress
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [preCheckoutQuery] is set
     */
    @TelegramCodegen.Type
    data class PreCheckoutQuery internal constructor(
        override val updateId: Long,
        val preCheckoutQuery: Payload
    ) : Parsed<PreCheckoutQuery.Payload> {
        override val payload: Payload = preCheckoutQuery
        override val date: Instant? = null

        /**
         * Telegram [PreCheckoutQuery](https://core.telegram.org/bots/api#precheckoutquery) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val id: String,
            val from: User,
            val currency: String,
            val totalAmount: Int,
            val invoicePayload: String,
            val shippingOptionId: String? = null,
            val orderInfo: OrderInfo? = null
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [purchasedPaidMedia] is set
     */
    @TelegramCodegen.Type
    data class PurchasedPaidMedia internal constructor(
        override val updateId: Long,
        val purchasedPaidMedia: Payload
    ) : Parsed<PurchasedPaidMedia.Payload> {
        override val payload: Payload = purchasedPaidMedia
        override val date: Instant? = null

        /**
         * Telegram [PreCheckoutQuery](https://core.telegram.org/bots/api#precheckoutquery) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val id: String,
            val from: User,
            val paidMediaPayload: String,
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [poll] is set
     */
    @TelegramCodegen.Type
    data class Poll internal constructor(
        override val updateId: Long,
        val poll: ski.gagar.vertigram.telegram.types.Poll
    ) : Parsed<ski.gagar.vertigram.telegram.types.Poll> {
        override val payload: ski.gagar.vertigram.telegram.types.Poll = poll
        override val date: Instant? = null

        companion object
    }

    /**
     * Case when [pollAnswer] is set
     */
    @TelegramCodegen.Type
    data class PollAnswer internal constructor(
        override val updateId: Long,
        val pollAnswer: ski.gagar.vertigram.telegram.types.Poll.Answer
    ) : Parsed<ski.gagar.vertigram.telegram.types.Poll.Answer> {
        override val payload: ski.gagar.vertigram.telegram.types.Poll.Answer = pollAnswer
        override val date: Instant? = null

        companion object
    }

    /**
     * Case when [myChatMember] is set
     *
     * Intentionally reuses [ChatMember.Payload]
     */
    @TelegramCodegen.Type
    data class MyChatMember internal constructor(
        override val updateId: Long,
        val myChatMember: ChatMember.Payload
    ) : Parsed<ChatMember.Payload> {
        override val payload: ChatMember.Payload = myChatMember
        override val date: Instant = payload.date

        companion object
    }

    /**
     * Case when [chatMember] is set
     */
    @TelegramCodegen.Type
    data class ChatMember internal constructor(
        override val updateId: Long,
        val chatMember: Payload
    ) : Parsed<ChatMember.Payload> {
        override val payload: Payload = chatMember
        override val date: Instant = payload.date

        /**
         * Telegram [ChatMemberUpdated](https://core.telegram.org/bots/api#chatmemberupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val from: User,
            val date: Instant,
            val oldChatMember: ski.gagar.vertigram.telegram.types.ChatMember,
            val newChatMember: ski.gagar.vertigram.telegram.types.ChatMember,
            val inviteLink: ChatInviteLink? = null,
            val viaJoinRequest: Boolean = false,
            val viaChatFolderInviteLink: Boolean = false
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [chatJoinRequest] is set
     */
    @TelegramCodegen.Type
    data class ChatJoinRequest internal constructor(
        override val updateId: Long,
        val chatJoinRequest: Payload
    ) : Parsed<ChatJoinRequest.Payload> {
        override val payload: Payload = chatJoinRequest
        override val date: Instant = payload.date

        /**
         * Telegram [ChatJoinRequest](https://core.telegram.org/bots/api#chatjoinrequest) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val from: User,
            val userChatId: Long,
            val date: Instant,
            val bio: String? = null,
            val inviteLink: ChatInviteLink? = null
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [chatBoost] is set
     */
    @TelegramCodegen.Type
    data class ChatBoost internal constructor(
        override val updateId: Long,
        val chatBoost: Payload
    ) : Parsed<ChatBoost.Payload> {
        override val payload: Payload = chatBoost
        override val date: Instant = payload.boost.addDate

        /**
         * Telegram [ChatBoostUpdated](https://core.telegram.org/bots/api#chatboostupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val boost: ski.gagar.vertigram.telegram.types.ChatBoost
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case when [chatBoostRemoved] is set
     */
    @TelegramCodegen.Type
    data class RemovedChatBoost internal constructor(
        override val updateId: Long,
        val chatBoostRemoved: Payload
    ) : Parsed<RemovedChatBoost.Payload> {
        override val payload: Payload = chatBoostRemoved
        override val date: Instant = payload.removeDate

        /**
         * Telegram [ChatBoostRemoved](https://core.telegram.org/bots/api#chatboostremoved) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        @TelegramCodegen.Type
        data class Payload internal constructor(
            val chat: Chat,
            val boostId: String,
            val removeDate: Instant,
            val source: ski.gagar.vertigram.telegram.types.ChatBoost.Source
        ) {
            companion object
        }

        companion object
    }

    /**
     * Case for malformed update
     */
    @TelegramCodegen.Type
    data class Malformed internal constructor(
        override val updateId: Long,
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
        @JsonProperty("message_reaction")
        MESSAGE_REACTION,
        @JsonProperty("message_reaction_COUNT")
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
