package ski.gagar.vertigram.verticles.telegram.address

import org.apache.commons.lang3.StringUtils
import ski.gagar.vertigram.telegram.types.Update

/**
 * A root object for resolving addresses for Telegram-related Vertigram verticles
 */
object TelegramAddress {
    /**
     * Default updates publishing address
     */
    const val UPDATES = "ski.gagar.vertigram.telegram.updates"

    /**
     * Default publishing address base for demultiplexed updates (i.e. with fields extracted)
     */
    const val DEMUX_BASE = "ski.gagar.vertigram.telegram.demux"

    /**
     * Default base address for Telegram verticle
     */
    const val TELEGRAM_VERTICLE_BASE = "ski.gagar.vertigram.telegram.verticle"

    /**
     * Get demultiplexed address from [className] and [base] address
     */
    private fun dispatchAddress(className: String, base: String) =
        "${base}.${StringUtils.uncapitalize(className)}"

    /**
     * Get demultiplexed address from [update] object and [base] address
     */
    fun dispatchAddress(update: Update<*>, base: String = DEMUX_BASE) =
        dispatchAddress(update.javaClass.simpleName, base)

    /**
     * Get demultiplexed address from update [type] and [base] address
     */
    fun dispatchAddress(type: Update.Type, base: String = DEMUX_BASE) =
        dispatchAddress(
            when (type) {
                Update.Type.MESSAGE -> Update.Message::class.simpleName
                Update.Type.EDITED_MESSAGE -> Update.EditedMessage::class.simpleName
                Update.Type.CHANNEL_POST -> Update.ChannelPost::class.simpleName
                Update.Type.EDITED_CHANNEL_POST -> Update.EditedChannelPost::class.simpleName
                Update.Type.BUSINESS_CONNECTION -> Update.BusinessConnection::class.simpleName
                Update.Type.BUSINESS_MESSAGE -> Update.BusinessMessage::class.simpleName
                Update.Type.EDITED_BUSINESS_MESSAGE -> Update.EditedBusinessMessage::class.simpleName
                Update.Type.DELETED_BUSINESS_MESSAGES -> Update.DeletedBusinessMessages::class.simpleName
                Update.Type.MESSAGE_REACTION -> Update.MessageReaction::class.simpleName
                Update.Type.MESSAGE_REACTION_COUNT -> Update.MessageReactionCount::class.simpleName
                Update.Type.INLINE_QUERY -> Update.InlineQuery::class.simpleName
                Update.Type.CHOSEN_INLINE_RESULT -> Update.ChosenInlineResult::class.simpleName
                Update.Type.CALLBACK_QUERY -> Update.CallbackQuery::class.simpleName
                Update.Type.SHIPPING_QUERY -> Update.ShippingQuery::class.simpleName
                Update.Type.PRE_CHECKOUT_QUERY -> Update.PreCheckoutQuery::class.simpleName
                Update.Type.PURCHASED_PAID_MEDIA -> Update.PurchasedPaidMedia::class.simpleName
                Update.Type.POLL -> Update.Poll::class.simpleName
                Update.Type.POLL_ANSWER -> Update.PollAnswer::class.simpleName
                Update.Type.MY_CHAT_MEMBER -> Update.MyChatMember::class.simpleName
                Update.Type.CHAT_MEMBER -> Update.ChatMember::class.simpleName
                Update.Type.CHAT_JOIN_REQUEST -> Update.ChatJoinRequest::class.simpleName
                Update.Type.CHAT_BOOST -> Update.ChatBoost::class.simpleName
                Update.Type.REMOVED_CHAT_BOOST -> Update.RemovedChatBoost::class.simpleName
            }!!,
            base
        )

    /**
     * Addresses for [ski.gagar.vertigram.verticles.telegram.AbstractTelegramDialogVerticle]
     */
    object Dialog {
        /**
         * Classifiers for private addresses
         */
        object Classifier {
            /**
             * Callback query classifier
             */
            const val CallbackQuery = "callbackQuery"

            /**
             * Message classifier
             */
            const val Message = "message"
        }
    }
}