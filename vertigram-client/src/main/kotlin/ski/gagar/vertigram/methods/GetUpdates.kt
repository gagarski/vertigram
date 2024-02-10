package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.types.UpdateType
import java.time.Duration

@Deprecated("Use Telegram.getUpdates instead")
@TelegramMethod(
    methodName = "getUpdates",
    generateVerticleConsumer = false
)
@TelegramCodegen(
    generateMethod = false
)
internal data class GetUpdatesRaw(
    val offset: Long? = null,
    val timeout: Duration = Duration.ZERO,
    val limit: Int? = null,
    val allowedUpdates: List<UpdateType>? = null
) :
    JsonTelegramCallable<List<Map<String, Any?>>>()
