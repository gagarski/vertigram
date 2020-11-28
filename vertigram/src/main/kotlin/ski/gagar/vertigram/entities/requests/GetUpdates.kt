package ski.gagar.vertigram.entities.requests

import ski.gagar.vertigram.entities.Update
import ski.gagar.vertigram.util.DoNotGenerateInTgVerticle

// I want to abstract user from longpolls/shortpolls, so getUpdates is a separate method and cannot be invoked with call
@Deprecated("Use Telegram.getUpdates instead")
@DoNotGenerateInTgVerticle
internal data class GetUpdates(val offset: Long? = null, val timeout: Long = 0, val limit: Long? = null) :
    JsonTgCallable<List<Update>>()
