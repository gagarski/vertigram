package ski.gagar.vxutil.vertigram.types

import ski.gagar.vxutil.vertigram.util.TgEnumName

/**
 * Allowed values for [MessageEntity.type]
 */
enum class EntityType {
    @TgEnumName("mention")
    MENTION,
    @TgEnumName("hashtag")
    HASHTAG,
    @TgEnumName("cashtag")
    CASHTAG,
    @TgEnumName("bot_command")
    BOT_COMMAND,
    @TgEnumName("url")
    URL,
    @TgEnumName("email")
    EMAIL,
    @TgEnumName("phone_number")
    PHONE_NUMBER,
    @TgEnumName("bold")
    BOLD,
    @TgEnumName("italic")
    ITALIC,
    @TgEnumName("underline")
    UNDERLINE,
    @TgEnumName("strikethrough")
    STRIKETHROUGH,
    @TgEnumName("spoiler")
    SPOILER,
    @TgEnumName("code")
    CODE,
    @TgEnumName("pre")
    PRE,
    @TgEnumName("text_link")
    TEXT_LINK,
    @TgEnumName("text_mention")
    TEXT_MENTION,
}

/**
 * Telegram type MessageEntity.
 */
data class MessageEntity(
    val type: EntityType,
    val offset: Int,
    val length: Int,
    val url: String? = null,
    val user: User? = null
)

/**
 * Telegram type MessageEntity after instantiation.
 */
data class InstantiatedEntity(
    val type: EntityType,
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
