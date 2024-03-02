package ski.gagar.vertigram.verticles.telegram

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.telegram.UpdateDispatcher.Config
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

/**
 * Update dispatcher that listens for [Update]s from [LongPoller] or [WebHook],
 * unwraps them (extracting the only field) and publishes them to address
 * [Config.publishBase].`updateType`, where `updateType` is the name of the only field
 * present in the update.
 */
class UpdateDispatcher : VertigramVerticle<UpdateDispatcher.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()

    override suspend fun start() {
        consumer<Update<*>, Unit>(typedConfig.listen) { dispatch(it) }
    }

    private fun dispatch(update: Update<*>) {
        val conf = typedConfig

        vertigram.eventBus.publish(TelegramAddress.dispatchAddress(update, conf.publishBase), update.payload)
    }

    data class Config(
        /**
         * Address to listen (should be the same as[UpdateReceiver.Config.updatePublishingAddress])
         */
        val listen: String = TelegramAddress.UPDATES,
        /**
         * Base address to publish dispatched updates
         */
        val publishBase: String = TelegramAddress.DEMUX_BASE
    )
}
