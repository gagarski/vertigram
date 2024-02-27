package ski.gagar.vertigram.telegram.throttling

import java.time.Duration

/**
 * Options for [ThrottlingTelegram]
 * See [Telegram Docs](https://core.telegram.org/bots/faq#broadcasting-to-users) for details
 */
data class ThrottlingOptions(
    /**
     * Length of "short burst" (mentioned in docs) that allowed to happen during 1 second in chat
     */
    val chatBurstPerSecond: Int? = 3,
    /**
     * Global per-second limit for bot
     */
    val globalPerSecond: Int? = 30,
    /**
     * Per-minute limit for chat
     */
    val chatPerMinute: Int? = 20,
    /**
     * Max time that throttled calls are allowed to wait before they throw [RateLimitExceededException]
     */
    val maxWait: Duration = Duration.ofSeconds(10)
)
