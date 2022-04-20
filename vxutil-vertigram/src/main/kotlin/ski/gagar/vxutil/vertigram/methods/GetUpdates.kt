package ski.gagar.vxutil.vertigram.methods

import ski.gagar.vxutil.vertigram.types.Update
import ski.gagar.vxutil.vertigram.util.DoNotGenerateInTgVerticle
import ski.gagar.vxutil.vertigram.util.TgMethodName
import java.time.Duration

// I want to abstract user from longpolls/shortpolls, so getUpdates is a separate method and cannot be invoked with call
/*
 * TODO remove me, currently used ONLY for type hints
 */
@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
internal data class GetUpdates(val offset: Long? = null, val timeout: Duration = Duration.ZERO, val limit: Int? = null) :
    JsonTgCallable<List<Update>>()

@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
@TgMethodName("getUpdates")
internal data class GetUpdatesRaw(val offset: Long? = null, val timeout: Duration = Duration.ZERO, val limit: Int? = null) :
    JsonTgCallable<List<Map<String, Any?>>>()
