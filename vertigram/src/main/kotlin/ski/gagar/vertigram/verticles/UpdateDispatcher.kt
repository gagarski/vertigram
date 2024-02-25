package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.core.type.TypeReference
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.types.Update

class UpdateDispatcher : VertigramVerticle<UpdateDispatcher.Config>() {
    override val configTypeReference: TypeReference<Config> = typeReference()

    override suspend fun start() {
        consumer<Update<*>, Unit>(typedConfig.addresses.listen) { dispatch(it) }
    }

    private fun dispatch(update: Update<*>) {
        val conf = typedConfig

        vertigram.eventBus.publish(VertigramAddresses.demuxAddress(update, conf.addresses.publishBase), update.payload)
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
