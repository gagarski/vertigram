package ski.gagar.vertigram.types

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.util.NoPosArgs
import java.time.Instant

class UpdateList(private val delegate: List<Update<*>>) : List<Update<*>> by delegate
class ParsedUpdateList(private val delegate: List<Update.Parsed<*>>) : List<Update.Parsed<*>> by delegate

/**
 * Telegram [Update](https://core.telegram.org/bots/api#update) type.
 *
 * The original type is split into subtypes given "You __must__ use exactly one of the optional fields."
 * condition, each of the subclasses represent the case of respecting field being set.
 *
 * Some classes which represent only the value of an update field are nested into that [Update] subtype
 * under name `Payload`, though there are exceptions for some fundamental types such as
 * [ski.gagar.vertigram.types.InlineQuery] or [ski.gagar.vertigram.types.Message].
 *
 * For up-to-date documentation please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes(
    JsonSubTypes.Type(value = Update.Message::class),
    JsonSubTypes.Type(value = Update.EditedMessage::class),
    JsonSubTypes.Type(value = Update.ChannelPost::class),
    JsonSubTypes.Type(value = Update.EditedChannelPost::class),
    JsonSubTypes.Type(value = Update.MessageReaction::class),
    JsonSubTypes.Type(value = Update.MessageReactionCount::class),
    JsonSubTypes.Type(value = Update.InlineQuery::class),
    JsonSubTypes.Type(value = Update.ChosenInlineResult::class),
    JsonSubTypes.Type(value = Update.CallbackQuery::class),
    JsonSubTypes.Type(value = Update.ShippingQuery::class),
    JsonSubTypes.Type(value = Update.PreCheckoutQuery::class),
    JsonSubTypes.Type(value = Update.Poll::class),
    JsonSubTypes.Type(value = Update.PollAnswer::class),
    JsonSubTypes.Type(value = Update.MyChatMember::class),
    JsonSubTypes.Type(value = Update.ChatMemberUpdated::class),
    JsonSubTypes.Type(value = Update.ChatJoinRequest::class),
    JsonSubTypes.Type(value = Update.ChatBoostUpdated::class),
    JsonSubTypes.Type(value = Update.ChatBoostRemoved::class),
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
    data class Message(
        override val updateId: Long,
        val message: ski.gagar.vertigram.types.Message
    ) : Parsed<ski.gagar.vertigram.types.Message> {
        override val payload: ski.gagar.vertigram.types.Message = message
        override val date: Instant = payload.date
    }

    /**
     * Case when [editedMessage] is set
     */
    data class EditedMessage(
        override val updateId: Long,
        val editedMessage: ski.gagar.vertigram.types.Message
    ) : Parsed<ski.gagar.vertigram.types.Message> {
        override val payload: ski.gagar.vertigram.types.Message = editedMessage
        override val date: Instant = payload.date

    }

    /**
     * Case when [channelPost] is set
     */
    data class ChannelPost(
        override val updateId: Long,
        val channelPost: ski.gagar.vertigram.types.Message
    ) : Parsed<ski.gagar.vertigram.types.Message> {
        override val payload: ski.gagar.vertigram.types.Message = channelPost
        override val date: Instant = payload.date
    }

    /**
     * Case when [editedChannelPost] is set
     */
    data class EditedChannelPost(
        override val updateId: Long,
        val editedChannelPost: ski.gagar.vertigram.types.Message
    ) : Parsed<ski.gagar.vertigram.types.Message> {
        override val payload: ski.gagar.vertigram.types.Message = editedChannelPost
        override val date: Instant = payload.date
    }

    /**
     * Case when [messageReaction] is set
     */
    data class MessageReaction(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chat: Chat,
            val messageId: Long,
            val user: User? = null,
            val actorChat: Chat? = null,
            val date: Instant,
            val oldReaction: List<ReactionType>,
            val newReaction: List<ReactionType>
        )
    }

    /**
     * Case when [messageReactionCount] is set
     */
    data class MessageReactionCount(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
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
            data class ReactionCount(
                @JsonIgnore
                private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
                val type: ReactionType,
                val totalCount: Int
            )

        }
    }

    /**
     * Case when [inlineQuery] is set
     */
    data class InlineQuery(
        override val updateId: Long,
        val inlineQuery: ski.gagar.vertigram.types.InlineQuery
    ) : Parsed<ski.gagar.vertigram.types.InlineQuery> {
        override val payload: ski.gagar.vertigram.types.InlineQuery = inlineQuery
        override val date: Instant? = null

    }

    /**
     * Case when [ChosenInlineResult] is set
     */
    data class ChosenInlineResult(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val resultId: String,
            val from: User,
            val location: Location? = null,
            val inlineMessageId: String? = null,
            val query: String
        )
    }

    /**
     * Case when [callbackQuery] is set
     */
    data class CallbackQuery(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val id: String,
            val from: User,
            val message: ski.gagar.vertigram.types.Message? = null,
            val inlineMessageId: String? = null,
            val chatInstance: String,
            val data: String? = null,
            val gameShortName: String? = null
        )
    }

    /**
     * Case when [shippingQuery] is set
     */
    data class ShippingQuery(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val id: String,
            val from: User,
            val invoicePayload: String,
            val shippingAddress: ShippingAddress
        )
    }

    /**
     * Case when [preCheckoutQuery] is set
     */
    data class PreCheckoutQuery(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val id: String,
            val from: User,
            val currency: String,
            val totalAmount: Int,
            val invoicePayload: String,
            val shippingOptionId: String? = null,
            val orderInfo: OrderInfo? = null
        )
    }

    /**
     * Case when [poll] is set
     */
    data class Poll(
        override val updateId: Long,
        val poll: ski.gagar.vertigram.types.Poll
    ) : Parsed<ski.gagar.vertigram.types.Poll> {
        override val payload: ski.gagar.vertigram.types.Poll = poll
        override val date: Instant? = null
    }

    /**
     * Case when [pollAnswer] is set
     */
    data class PollAnswer(
        override val updateId: Long,
        val pollAnswer: ski.gagar.vertigram.types.PollAnswer
    ) : Parsed<ski.gagar.vertigram.types.PollAnswer> {
        override val payload: ski.gagar.vertigram.types.PollAnswer = pollAnswer
        override val date: Instant? = null

    }

    /**
     * Case when [myChatMember] is set
     *
     * Intentionally reuses [ChatMemberUpdated.Payload]
     */
    data class MyChatMember(
        override val updateId: Long,
        val myChatMember: ChatMemberUpdated.Payload
    ) : Parsed<ChatMemberUpdated.Payload> {
        override val payload: ChatMemberUpdated.Payload = myChatMember
        override val date: Instant = payload.date

    }

    /**
     * Case when [chatMemberUpdated] is set
     */
    data class ChatMemberUpdated(
        override val updateId: Long,
        val chatMemberUpdated: Payload
    ) : Parsed<ChatMemberUpdated.Payload> {
        override val payload: Payload = chatMemberUpdated
        override val date: Instant = payload.date

        /**
         * Telegram [ChatMemberUpdated](https://core.telegram.org/bots/api#chatmemberupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chat: Chat,
            val from: User,
            val date: Instant,
            val oldChatMember: ChatMember,
            val newChatMember: ChatMember,
            val inviteLink: ChatInviteLink? = null,
            val viaChatFolderInviteLink: Boolean = false
        )
    }

    /**
     * Case when [chatJoinRequest] is set
     */
    data class ChatJoinRequest(
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
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chat: Chat,
            val from: User,
            val userChatId: Long,
            val date: Instant,
            val bio: String? = null,
            val inviteLink: ChatInviteLink? = null
        )
    }

    /**
     * Case when [chatBoostUpdated] is set
     */
    data class ChatBoostUpdated(
        override val updateId: Long,
        val chatBoostUpdated: Payload
    ) : Parsed<ChatBoostUpdated.Payload> {
        override val payload: Payload = chatBoostUpdated
        override val date: Instant = payload.boost.addDate

        /**
         * Telegram [ChatBoostUpdated](https://core.telegram.org/bots/api#chatboostupdated) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chat: Chat,
            val boost: ChatBoost
        )
    }

    /**
     * Case when [chatBoostRemoved] is set
     */
    data class ChatBoostRemoved(
        override val updateId: Long,
        val chatBoostRemoved: Payload
    ) : Parsed<ChatBoostRemoved.Payload> {
        override val payload: Payload = chatBoostRemoved
        override val date: Instant = payload.removeDate

        /**
         * Telegram [ChatBoostRemoved](https://core.telegram.org/bots/api#chatboostremoved) type.
         *
         * For up-to-date documentation please consult the official Telegram docs.
         */
        data class Payload(
            @JsonIgnore
            private val noPosArgs: NoPosArgs = NoPosArgs.INSTANCE,
            val chat: Chat,
            val boostId: String,
            val removeDate: Instant,
            val source: ChatBoost.Source
        )
    }

    /**
     * Case for malformed update
     */
    data class Malformed(
        override val updateId: Long,
        val malformedRawData: Map<String, Any?>
    ) : Update<Map<String, Any?>> {
        override val payload: Map<String, Any?> = malformedRawData
        override val date: Instant? = null
    }
}
