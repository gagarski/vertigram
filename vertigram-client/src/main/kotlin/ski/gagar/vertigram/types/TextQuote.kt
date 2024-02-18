package ski.gagar.vertigram.types

import ski.gagar.vertigram.types.richtext.HasOptionalTextWithEntities

data class TextQuote(
    override val text: String,
    val position: Int,
    override val entities: List<MessageEntity>? = null,
    @get:JvmName("getIsManual")
    val isManual: Boolean = false
) : HasOptionalTextWithEntities
