package ski.gagar.vxutil.vertigram.types

data class InlineQueryResultContact(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null,
    val replyMarkup: InlineKeyboardMarkup? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbUrl: String? = null,
    val thumbWidth: Int? = null,
    val thumbHeight: Int? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.CONTACT
}
