package ski.gagar.vertigram.methods

import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.UpdateType
import ski.gagar.vertigram.util.DoNotGenerateInTgVerticle
import ski.gagar.vertigram.util.TgMethodName
import java.time.Duration

// I want to abstract user from longpolls/shortpolls, so getUpdates is a separate method and cannot be invoked with call
/*
 * TODO remove me, currently used ONLY for type hints
 */
@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
data class GetUpdates(
    val offset: Long? = null,
    val timeout: Duration = Duration.ZERO,
    val limit: Int? = null,
    val allowedUpdates: List<UpdateType>? = null
) :
    JsonTgCallable<List<Update>>()

@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
@TgMethodName("getUpdates")
internal data class GetUpdatesRaw(
    val offset: Long? = null,
    val timeout: Duration = Duration.ZERO,
    val limit: Int? = null,
    val allowedUpdates: List<UpdateType>? = null
) :
    JsonTgCallable<List<Map<String, Any?>>>()
