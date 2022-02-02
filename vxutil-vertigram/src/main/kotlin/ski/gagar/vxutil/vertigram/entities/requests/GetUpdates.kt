package ski.gagar.vxutil.vertigram.entities.requests

import ski.gagar.vxutil.vertigram.entities.ParsedUpdate
import ski.gagar.vxutil.vertigram.entities.Update
import ski.gagar.vxutil.vertigram.util.DoNotGenerateInTgVerticle
import ski.gagar.vxutil.vertigram.util.TgMethodName

// I want to abstract user from longpolls/shortpolls, so getUpdates is a separate method and cannot be invoked with call
/*
 * TODO remove me, currently used ONLY for type hints
 */
@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
internal data class GetUpdates(val offset: Long? = null, val timeout: Long = 0, val limit: Long? = null) :
    JsonTgCallable<List<Update>>()

@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
@TgMethodName("getUpdates")
internal data class GetUpdatesRaw(val offset: Long? = null, val timeout: Long = 0, val limit: Long? = null) :
    JsonTgCallable<List<Map<String, Any?>>>()
