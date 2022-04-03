package ski.gagar.vxutil.vertigram.types

/**
 * Telegram type Invoice.
 */
data class Invoice(
    val title: String,
    val description: String,
    val startParameter: String,
    val currency: String,
    val totalAmount: Long
)
