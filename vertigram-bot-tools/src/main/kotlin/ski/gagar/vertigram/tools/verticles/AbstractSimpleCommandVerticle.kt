package ski.gagar.vertigram.tools.verticles

import ski.gagar.vertigram.jackson.suspendJsonConsumer
import ski.gagar.vertigram.tools.isCommandForBot
import ski.gagar.vertigram.tools.isForwarded
import ski.gagar.vertigram.types.Message
import ski.gagar.vertigram.types.Update
import ski.gagar.vertigram.types.User
import ski.gagar.vertigram.verticles.BaseVertigramVerticle
import ski.gagar.vertigram.verticles.VertigramAddresses

abstract class AbstractSimpleCommandVerticle : BaseVertigramVerticle() {
    abstract val command: String
    abstract suspend fun respond(message: Message)
    open val listenAddress: String = VertigramAddresses.demuxAddress(Update.Type.MESSAGE)

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

    protected abstract val me: User.Me?
}
