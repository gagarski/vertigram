package ski.gagar.vertigram.verticles.telegram

import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.isForwarded
import ski.gagar.vertigram.telegram.types.util.isCommandForBot
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

abstract class AbstractSimpleCommandVerticle<Config : AbstractSimpleCommandVerticle.Config> : VertigramVerticle<Config>() {
    abstract val command: String
    abstract suspend fun respond(message: Message)
    open val listenAddress: String
        get() = TelegramAddress.demuxAddress(Update.Type.MESSAGE, typedConfig.baseAddress)

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

    interface Config {
        val me: User.Me?
        val baseAddress: String
    }
}
