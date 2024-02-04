package ski.gagar.vertigram.types

import java.time.Duration

data class InputLocationMessageContent(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null,
    val livePeriod: Duration? = null,
    val heading: Int? = null,
    val proximityAlertRadius: Int? = null,
) : InputMessageContent
