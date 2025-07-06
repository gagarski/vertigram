package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Telegram [OwnedGift](https://core.telegram.org/bots/api#ownedgift) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(
    JsonSubTypes.Type(value = OwnedGift.Regular::class, name = OwnedGift.Type.REGULAR_STR),
    JsonSubTypes.Type(value = OwnedGift.Unique::class, name = OwnedGift.Type.UNIQUE_STR),

)
sealed interface OwnedGift {
    val type: Type
    val ownedGiftId: String?
    val senderUser: User?
    val sendDate: Instant
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getIsSaved")
    val isSaved: Boolean

    /**
     * Telegram [OwnedGiftRegular](https://core.telegram.org/bots/api#ownedgiftregular) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type(wrapRichText = false)
    data class Regular internal constructor(
        val gift: Gift,
        override val ownedGiftId: String? = null,
        override val senderUser: User? = null,
        override val sendDate: Instant,
        val text: String? = null,
        val entities: List<MessageEntity>? = null,
        @get:JvmName("getIsPrivate")
        val isPrivate: Boolean = false,
        @get:JvmName("getIsSaved")
        override val isSaved: Boolean = false,
        val canBeUpgraded: Boolean = false,
        val wasRefunded: Boolean = false,
        val convertStarCount: Int? = null,
        val prepaidUpgradeStarCount: Int? = null
    ) : OwnedGift {
        override val type: Type = Type.REGULAR
        companion object
    }

    /**
     * Telegram [OwnedGiftUnique](https://core.telegram.org/bots/api#ownedgiftunique) type.
     *
     * For up-to-date documentation, please consult the official Telegram docs.
     */
    @TelegramCodegen.Type
    data class Unique internal constructor(
        val gift: UniqueGift,
        override val ownedGiftId: String? = null,
        override val senderUser: User? = null,
        override val sendDate: Instant,
        @get:JvmName("getIsSaved")
        override val isSaved: Boolean = false,
        val canBeTransfered: Boolean = false,
        val transferStarCount: Int? = null,
        val nextTransferDate: Instant? = null
    ) : OwnedGift {
        override val type: Type = Type.UNIQUE
        companion object
    }

    /**
     * A value for [OwnedGift.type] field.
     */
    enum class Type {
        @JsonProperty("regular")
        REGULAR,
        @JsonProperty("unique")
        UNIQUE;
        companion object {
            const val REGULAR_STR = "regular";
            const val UNIQUE_STR = "unique";
        }
    }
    companion object
}