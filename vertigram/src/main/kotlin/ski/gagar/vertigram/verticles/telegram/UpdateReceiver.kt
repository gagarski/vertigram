package ski.gagar.vertigram.verticles.telegram

import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.verticles.common.VertigramVerticle

abstract class UpdateReceiver<T : UpdateReceiver.Config> : VertigramVerticle<T>() {
    interface Config {
        val allowedUpdates: List<Update.Type>
        val telegramAddress: String
        val updatePublishingAddress: String
        val skipMissed: Boolean
    }
}