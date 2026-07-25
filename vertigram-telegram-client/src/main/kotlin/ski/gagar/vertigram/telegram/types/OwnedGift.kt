package ski.gagar.vertigram.telegram.types

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.time.Instant

/**
 * Describes a gift received and owned by a user or a chat.
 *
 * See Telegram's [OwnedGift](https://core.telegram.org/bots/api#ownedgift) documentation.
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
     * Case when the owned gift is a regular gift.
     *
     * See Telegram's [OwnedGiftRegular](https://core.telegram.org/bots/api#ownedgiftregular) documentation.
     */
    @TelegramCodegen.Type
    data class Regular internal constructor(
        /** Information about the regular gift. */
        val gift: Gift,
        /** Unique identifier of the gift for the business account. */
        override val ownedGiftId: String? = null,
        /** User that sent the gift. */
        override val senderUser: User? = null,
        /** Date the gift was sent. */
        override val sendDate: Instant,
        /** Text added to the gift. */
        val text: String? = null,
        /** Special entities that appear in [text]. */
        val entities: List<MessageEntity>? = null,
        /** Whether the sender and gift text are shown only to the gift receiver. */
        @get:JvmName("getIsPrivate")
        val isPrivate: Boolean = false,
        /** Whether the gift is displayed on the account's profile page. */
        @get:JvmName("getIsSaved")
        override val isSaved: Boolean = false,
        /** Whether the gift can be upgraded to a unique gift. */
        val canBeUpgraded: Boolean = false,
        /** Whether the gift was refunded and is no longer available. */
        val wasRefunded: Boolean = false,
        /** Number of Telegram Stars that can be claimed instead of the gift. */
        val convertStarCount: Int? = null,
        /** Number of Telegram Stars prepaid for the gift upgrade. */
        val prepaidUpgradeStarCount: Int? = null,
        /** Whether the gift's upgrade was paid separately from the gift purchase. */
        @get:JvmName("getIsUpgradeSeparate")
        val isUpgradeSeparate: Boolean = false,
        /** Number of the upgraded gift. */
        val uniqueGiftNumber: Int? = null
    ) : OwnedGift {
        override val type: Type = Type.REGULAR
        companion object
    }

    /**
     * Case when the owned gift is a unique gift.
     *
     * See Telegram's [OwnedGiftUnique](https://core.telegram.org/bots/api#ownedgiftunique) documentation.
     */
    @TelegramCodegen.Type
    data class Unique internal constructor(
        /** Information about the unique gift. */
        val gift: UniqueGift,
        /** Unique identifier of the gift for the business account. */
        override val ownedGiftId: String? = null,
        /** User that sent the gift. */
        override val senderUser: User? = null,
        /** Date the gift was sent. */
        override val sendDate: Instant,
        /** Whether the gift is displayed on the account's profile page. */
        @get:JvmName("getIsSaved")
        override val isSaved: Boolean = false,
        /** Whether the gift can be transferred to another owner. */
        val canBeTransfered: Boolean = false,
        /** Number of Telegram Stars required to transfer the gift. */
        val transferStarCount: Int? = null,
        /** Earliest date when the gift can be transferred. */
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
