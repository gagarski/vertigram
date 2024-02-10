package ski.gagar.vertigram.methods

import ski.gagar.vertigram.annotations.TelegramCodegen
import ski.gagar.vertigram.annotations.TelegramMethod
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateType
import java.time.Duration

/*
 * TODO remove me, currently used ONLY for type hints
 */
@Deprecated("Use Telegram.getUpdates instead")
@TelegramMethod(
    methodName = "getUpdates",
    generateVerticleConsumer = false
)
data class GetUpdates(
    val offset: Long? = null,
    val timeout: Duration = Duration.ZERO,
    val limit: Int? = null,
    val allowedUpdates: List<UpdateType>? = null
) :
    JsonTelegramCallable<List<Update>>()

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
