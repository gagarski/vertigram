package ski.gagar.vertigram.verticles.telegram

import ski.gagar.vertigram.telegram.types.Message
import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.telegram.types.User
import ski.gagar.vertigram.telegram.types.isForwarded
import ski.gagar.vertigram.telegram.types.util.isCommandForBot
import ski.gagar.vertigram.verticles.common.VertigramVerticle
import ski.gagar.vertigram.verticles.telegram.address.TelegramAddress

/**
 * Handle a command without saving any state
 */
abstract class AbstractSimpleCommandVerticle<Config : AbstractSimpleCommandVerticle.Config> : VertigramVerticle<Config>() {
    /**
     * Command to handle (/command or /command@me).
     *
     * To be overridden by subclass.
     */
    abstract val command: String

    /**
     * Respond to the message with command.
     *
     * To be overridden by subclass.
     */
    abstract suspend fun respond(message: Message)
    open val listenAddress: String
        get() = TelegramAddress.dispatchAddress(Update.Type.MESSAGE, typedConfig.baseAddress)

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

    /**
     * Base interface for configuration
     */
    interface Config {
        /**
         * Bot user information, returned by `getMe` method.
         */
        val me: User.Me?

        /**
         * Base address to receive demultiplexed updates
         */
        val baseAddress: String
    }
}
