package ski.gagar.vertigram.util

import ski.gagar.vertigram.types.MessageEntity
import ski.gagar.vertigram.types.MessageEntityType
import ski.gagar.vertigram.types.User

data class InstantiatedEntity(
    val type: MessageEntityType,
    val text: String,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null
) {
    constructor(entity: MessageEntity, text: String?) :
            this(
                entity.type,
                text.safeSubstring(entity.offset, entity.length),
                entity.offset,
                entity.length,
                entity.url,
                entity.user
            )

    companion object {
        private fun String?.safeSubstring(offset: Int, length: Int): String {
            this ?: throw BadEntityException

            if (offset < 0 || length < 0)
                throw BadEntityException

            if (this.length < offset + length)
                throw BadEntityException

            return this.substring(offset, offset + length)
        }
    }
}

object BadEntityException: Exception("Bad telegram entity")

fun String?.withEntities(entities: List<MessageEntity>?) =
    entities?.map { InstantiatedEntity(it, this) } ?: listOf()

fun List<MessageEntity>?.withText(text: String?) = text.withEntities(this)
