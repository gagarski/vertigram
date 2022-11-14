package ski.gagar.vxutil.vertigram.tools.verticles

import ski.gagar.vxutil.jackson.suspendJsonConsumer
import ski.gagar.vxutil.verticles.ErrorLoggingCoroutineVerticle
import ski.gagar.vxutil.vertigram.tools.isCommandForBot
import ski.gagar.vxutil.vertigram.tools.isForwarded
import ski.gagar.vxutil.vertigram.tools.verticles.address.VertigramAddress
import ski.gagar.vxutil.vertigram.types.Me
import ski.gagar.vxutil.vertigram.types.Message

abstract class AbstractSimpleCommandVerticle : ErrorLoggingCoroutineVerticle() {
    abstract val command: String
    abstract suspend fun respond(message: Message)
    open val listenAddress: String = VertigramAddress.Message

    override suspend fun start() {
        suspendJsonConsumer<Message, Unit>(listenAddress) { handle(it) }
    }

    private val Message.isForMe
        get() = isCommandForBot(command, me)

    private suspend fun handle(msg: Message) {
        if (!msg.isForMe) return
        if (msg.isForwarded) return

        respond(msg)
    }

    protected abstract val me: Me?
}
