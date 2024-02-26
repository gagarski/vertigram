package ski.gagar.vertigram.types.util

import java.time.Instant

/**
 * Return [this] or [Instant.EPOCH] if [this] is `null`
 *
 * Used in [ski.gagar.vertigram.types.ChatMember.Banned] and
 * [ski.gagar.vertigram.types.ChatMember.Restricted] to deal with Telegram strange formats for these dates
 */
fun Instant?.orEpoch(): Instant = this ?: Instant.EPOCH

/**
 * Return [this] or `null` if [this] is [Instant.EPOCH]
 *
 * Used in [ski.gagar.vertigram.types.ChatMember.Banned] and
 * [ski.gagar.vertigram.types.ChatMember.Restricted] to deal with Telegram strange formats for these dates
 */
fun Instant.nullIfEpoch() = if (toEpochMilli() == 0L) null else this