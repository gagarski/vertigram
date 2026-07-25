package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.telegram.types.colors.RgbColor

/**
 * This object represents a gift that can be sent by the bot.
 *
 * See Telegram's [Gift](https://core.telegram.org/bots/api#gift) documentation.
 */
@TelegramCodegen.Type
data class Gift internal constructor(
    /** Unique identifier of the gift. */
    val id: String,
    /** The sticker that represents the gift. */
    val sticker: Sticker,
    /** The number of Telegram Stars that must be paid to send the sticker. */
    val starCount: Int,
    /** The number of Telegram Stars that must be paid to upgrade the gift to a unique one. */
    val upgradeStarCount: Int? = null,
    /** The total number of gifts of this type that can be sent by all users; for limited gifts only. */
    val totalCount: Int? = null,
    /** The number of remaining gifts of this type that can be sent by all users; for limited gifts only. */
    val remainingCount: Int? = null,
    /** Information about the chat that published the gift. */
    val publisherChat: Chat? = null,
    /** The total number of gifts of this type that can be sent by the bot; for limited gifts only. */
    val personalTotalCount: Int? = null,
    /** The number of remaining gifts of this type that can be sent by the bot; for limited gifts only. */
    val personalRemainingCount: Int? = null,
    /** `true` if the gift can only be purchased by Telegram Premium subscribers. */
    @get:JvmName("getIsPremium")
    val isPremium: Boolean = false,
    /** `true` if the gift can be used, after being upgraded, to customize a user's appearance. */
    val hasColors: Boolean = false,
    /** Background of the gift. */
    val background: Background? = null,
    /** The total number of different unique gifts that can be obtained by upgrading the gift. */
    val uniqueGiftVariantCount: Int? = null
) {
    /**
     * This object describes the background of a gift.
     *
     * See Telegram's [GiftBackground](https://core.telegram.org/bots/api#giftbackground) documentation.
     */
    @TelegramCodegen.Type
    data class Background internal constructor(
        /** Center color of the background in RGB format. */
        val centerColor: RgbColor,
        /** Edge color of the background in RGB format. */
        val edgeColor: RgbColor,
        /** Text color of the background in RGB format. */
        val textColor: RgbColor
    ) {
        companion object
    }

    companion object
}
