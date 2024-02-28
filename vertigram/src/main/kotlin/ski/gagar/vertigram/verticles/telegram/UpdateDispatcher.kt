package ski.gagar.vertigram.verticles.telegram

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

class UpdateDispatcher : VertigramVerticle<UpdateDispatcher.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()

    override suspend fun start() {
        consumer<Update<*>, Unit>(typedConfig.listen) { dispatch(it) }
    }

    private fun dispatch(update: Update<*>) {
        val conf = typedConfig

        vertigram.eventBus.publish(TelegramAddress.demuxAddress(update, conf.publishBase), update.payload)
    }

    data class Config(
        val listen: String = TelegramAddress.UPDATES,
        val publishBase: String = TelegramAddress.DEMUX_BASE
    )
}
