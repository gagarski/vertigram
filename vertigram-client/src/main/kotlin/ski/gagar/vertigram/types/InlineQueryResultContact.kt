package ski.gagar.vertigram.types

data class InlineQueryResultContact(
    val id: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String? = null,
    val userId: Long? = null,
    val vcard: String? = null,
    val replyMarkup: ReplyMarkup.InlineKeyboard? = null,
    val inputMessageContent: InputMessageContent? = null,
    val thumbnailUrl: String? = null,
    val thumbnailWidth: Int? = null,
    val thumbnailHeight: Int? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.CONTACT
}
