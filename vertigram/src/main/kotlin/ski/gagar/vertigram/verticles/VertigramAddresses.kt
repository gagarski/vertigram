package ski.gagar.vertigram.verticles

import org.apache.commons.lang3.StringUtils
import ski.gagar.vertigram.types.Update

object VertigramAddresses {
    const val UPDATES = "ski.gagar.vertigram.telegram.updates"
    const val DEMUX_BASE = "ski.gagar.vertigram.telegram.demux"
    const val TELEGRAM_VERTICLE_BASE = "ski.gagar.vertigram.telegram.verticle"

    private fun demuxAddress(className: String, base: String) =
        "${base}.${StringUtils.uncapitalize(className)}"

    fun demuxAddress(update: Update<*>, base: String = DEMUX_BASE) =
        demuxAddress(update.javaClass.simpleName, base)

    fun demuxAddress(type: Update.Type, base: String = DEMUX_BASE) =
        demuxAddress(
            when (type) {
                Update.Type.MESSAGE -> Update.Message::class.simpleName
                Update.Type.EDITED_MESSAGE -> Update.EditedMessage::class.simpleName
                Update.Type.CHANNEL_POST -> Update.ChannelPost::class.simpleName
                Update.Type.EDITED_CHANNEL_POST -> Update.EditedChannelPost::class.simpleName
                Update.Type.MESSAGE_REACTION -> Update.MessageReaction::class.simpleName
                Update.Type.MESSAGE_REACTION_COUNT -> Update.MessageReactionCount::class.simpleName
                Update.Type.INLINE_QUERY -> Update.InlineQuery::class.simpleName
                Update.Type.CHOSEN_INLINE_RESULT -> Update.ChosenInlineResult::class.simpleName
                Update.Type.CALLBACK_QUERY -> Update.CallbackQuery::class.simpleName
                Update.Type.SHIPPING_QUERY -> Update.ShippingQuery::class.simpleName
                Update.Type.PRE_CHECKOUT_QUERY -> Update.PreCheckoutQuery::class.simpleName
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
}