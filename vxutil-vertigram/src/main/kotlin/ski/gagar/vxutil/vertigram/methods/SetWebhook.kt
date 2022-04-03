package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.util.TgEnumName

enum class UpdateType {
    @TgEnumName("message")
    MESSAGE,
    @TgEnumName("edited_message")
    EDITED_MESSAGE,
    @TgEnumName("channel_post")
    CHANNEL_POST,
    @TgEnumName("edited_channel_post")
    EDITED_CHANNEL_POST,
    @TgEnumName("inline_query")
    INLINE_QUERY,
    @TgEnumName("chosen_inline_result")
    CHOSEN_INLINE_RESULT,
    @TgEnumName("callback_query")
    CALLBACK_QUERY,
    @TgEnumName("shipping_query")
    SHIPPING_QUERY,
    @TgEnumName("pre_checkout_query")
    PRE_CHECKOUT_QUERY;

    override fun toString(): String = super.toString().lowercase()

}

data class SetWebhook(
    val url: String,
    val maxConnections: Int? = null,
    val allowedUpdates: List<UpdateType>? = null
) : JsonTgCallable<Boolean>()
