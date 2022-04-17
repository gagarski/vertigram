package ski.gagar.vxutil.vertigram.types

import com.fasterxml.jackson.annotation.JsonProperty

enum class EntityType {
    @JsonProperty("mention")
    MENTION,
    @JsonProperty("hashtag")
    HASHTAG,
    @JsonProperty("cashtag")
    CASHTAG,
    @JsonProperty("bot_command")
    BOT_COMMAND,
    @JsonProperty("url")
    URL,
    @JsonProperty("email")
    EMAIL,
    @JsonProperty("phone_number")
    PHONE_NUMBER,
    @JsonProperty("bold")
    BOLD,
    @JsonProperty("italic")
    ITALIC,
    @JsonProperty("underline")
    UNDERLINE,
    @JsonProperty("strikethrough")
    STRIKETHROUGH,
    @JsonProperty("spoiler")
    SPOILER,
    @JsonProperty("code")
    CODE,
    @JsonProperty("pre")
    PRE,
    @JsonProperty("text_link")
    TEXT_LINK,
    @JsonProperty("text_mention")
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
