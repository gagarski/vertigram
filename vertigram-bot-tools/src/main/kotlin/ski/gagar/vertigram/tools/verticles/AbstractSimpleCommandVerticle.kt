package ski.gagar.vertigram.tools.verticles

import ski.gagar.vertigram.tools.HasMe
import ski.gagar.vertigram.tools.isCommandForBot
import ski.gagar.vertigram.tools.isForwarded
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.verticles.VertigramAddresses
import ski.gagar.vertigram.verticles.VertigramVerticle

abstract class AbstractSimpleCommandVerticle<Config : AbstractSimpleCommandVerticle.Config> : VertigramVerticle<Config>() {
    abstract val command: String
    abstract suspend fun respond(message: Message)
    open val listenAddress: String = VertigramAddresses.demuxAddress(Update.Type.MESSAGE, typedConfig.baseAddress)

    override suspend fun start() {
        consumer<Message, Unit>(listenAddress) { handle(it) }
    }

    private val Message.isForMe
        get() = isCommandForBot(command, typedConfig.me)

    private suspend fun handle(msg: Message) {
        if (!msg.isForMe) return
        if (msg.isForwarded) return

        respond(msg)
    }

    interface Config : HasMe {
        val baseAddress: String
            get() = VertigramAddresses.DEMUX_BASE
    }
}
