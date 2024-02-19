package ski.gagar.vertigram.tools.messages

import ski.gagar.vertigram.types.MessageEntity

data class InstantiatedEntity(
    val textPart: String,
    val entity: MessageEntity
)

private fun String?.safeSubstring(offset: Int, length: Int): String {
    this ?: throw BadEntityException("Bad telegram entity")

    if (offset < 0 || length < 0)
        throw BadEntityException("Bad telegram entity")

    if (this.length < offset + length)
        throw BadEntityException("Bad telegram entity")

    return this.substring(offset, offset + length)
}

fun MessageEntity.instantiateFrom(fullText: String) =
    InstantiatedEntity(textPart = fullText.safeSubstring(this.offset, this.length), entity = this)


class BadEntityException(override val message: String): Exception(message)

fun String.withEntities(entities: List<MessageEntity>) =
    entities.map { it.instantiateFrom(this) }

fun List<MessageEntity>?.withText(text: String) = text.withEntities(this ?: listOf())
