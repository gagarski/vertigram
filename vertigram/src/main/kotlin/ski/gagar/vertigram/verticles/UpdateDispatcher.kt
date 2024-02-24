package ski.gagar.vertigram.verticles

import ski.gagar.vertigram.jackson.mapTo
import ski.gagar.vertigram.jackson.publishJson
import ski.gagar.vertigram.jackson.suspendJsonConsumer
import ski.gagar.vertigram.types.Update

class UpdateDispatcher : BaseVertigramVerticle() {
    private val typedConf: Config by lazy {
        config.mapTo()
    }
    override suspend fun start() {
        suspendJsonConsumer<Update<*>, Unit>(typedConf.addresses.listen) { dispatch(it) }
    }

    private fun dispatch(update: Update<*>) {
        val conf = typedConf

        vertx.eventBus().publishJson(VertigramAddresses.demuxAddress(update, conf.addresses.publishBase), update.payload)
    }

    data class Config(
        val addresses: Addresses = Addresses()
    ) {
        data class Addresses(
            val listen: String = VertigramAddresses.UPDATES,
            val publishBase: String = VertigramAddresses.DEMUX_BASE
        )
    }
}
