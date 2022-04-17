package ski.gagar.vxutil.vertigram.types

import java.time.Instant

data class WebhookInfo(
    val url: String,
    val hasCustomCertificate: Boolean,
    val pendingUpdateCount: Int,
    val ipAddress: String? = null,
    val lastErrorDate: Instant? = null,
    val lastErrorMessage: String? = null,
    val lastSynchronizationErrorDate: Instant? = null,
    val maxConnections: Int? = null,
    val allowedUpdates: List<String>? = null
)
