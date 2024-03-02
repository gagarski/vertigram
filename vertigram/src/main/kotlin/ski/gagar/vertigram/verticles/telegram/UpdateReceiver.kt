package ski.gagar.vertigram.verticles.telegram

import ski.gagar.vertigram.telegram.types.Update
import ski.gagar.vertigram.verticles.common.VertigramVerticle

/**
 * Common superclass for [LongPoller] and [WebHook]
 */
abstract class UpdateReceiver<T : UpdateReceiver.Config> : VertigramVerticle<T>() {
    /**
     * Common interface for [LongPoller.Config] and [WebHook.Config]
     */
    interface Config {
        /**
         * Allowed updates
         */
        val allowedUpdates: List<Update.Type>

        /**
         * [TelegramVerticle] address
         */
        val telegramAddress: String

        /**
         * Address to publish received updates
         */
        val updatePublishingAddress: String

        /**
         * Should skip updates with the date earler than [UpdateReceiver] is deployed
         */
        val skipMissed: Boolean
    }
}