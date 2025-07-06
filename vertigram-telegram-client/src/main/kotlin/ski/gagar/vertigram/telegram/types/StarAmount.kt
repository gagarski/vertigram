package ski.gagar.vertigram.telegram.types

import ski.gagar.vertigram.annotations.TelegramCodegen
import java.math.BigDecimal

/**
 * Telegram [StarAmount](https://core.telegram.org/bots/api#staramount) type.
 *
 * For up-to-date documentation, please consult the official Telegram docs.
 */
@TelegramCodegen.Type
data class StarAmount internal constructor(
    val amount: Int,
    val nanostarAmount: Int
) {
    init {
        if (amount < 0) {
            require(nanostarAmount <= 0) {
                "amount and nanostarAmount sign should be the same"
            }
        }
        if (amount > 0) {
            require(nanostarAmount >= 0) {
                "amount and nanostarAmount sign should be the same"
            }
        }
    }

    val bigDecimalValue: BigDecimal
        get() {
            return (BigDecimal.valueOf(amount.toLong()) +
                    BigDecimal.valueOf(nanostarAmount.toLong()) * NANO).stripTrailingZeros()
        }

    companion object {
        val NANO = BigDecimal("1E-9")

        fun from(decimal: BigDecimal): StarAmount {
            val sign = decimal.signum()
            val abs = decimal.abs()
            var amount = abs.toInt()
            var nano = ((abs % BigDecimal.ONE) / NANO).toInt()

            val lessThanNano = abs % NANO;

            check(lessThanNano.stripTrailingZeros() == BigDecimal.ZERO) {
                "Less then nano precision is not supported"
            }

            if (sign < 0) {
                amount = -amount
                nano = -nano
            }
            return StarAmount(amount, nano)
        }
    }
}

fun BigDecimal.toStarAmount() = StarAmount.from(this)
