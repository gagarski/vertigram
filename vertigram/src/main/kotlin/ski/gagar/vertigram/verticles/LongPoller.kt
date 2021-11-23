package ski.gagar.vertigram.verticles

import kotlinx.coroutines.launch
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.client.TgVTelegram
import ski.gagar.vertigram.entities.requests.DeleteWebhook
import ski.gagar.vertigram.messages.UpdateList
import ski.gagar.vxutil.ErrorLoggingCoroutineVerticle
import ski.gagar.vxutil.jackson.mapTo
import ski.gagar.vxutil.jackson.publishJson
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.retrying
import ski.gagar.vxutil.sleep
import java.time.Instant

class LongPoller: ErrorLoggingCoroutineVerticle() {
    private val typedConfig by lazy {
        config.mapTo<Config>()
    }
    private val tg: Telegram by lazy {
        TgVTelegram(vertx, typedConfig.tgvAddress)
    }

    private var offset: Long? = null
    private lateinit var startDate: Instant

    override suspend fun start() {
        logger.info("Deleting old Telegram webhook if any...")
        startDate = Instant.now()

        retrying(coolDown = { sleep(3000) }) {
            tg.call(DeleteWebhook)
        }

        launch {
            doWork()
        }
    }

    private suspend fun doWork() {
        while (true) {
            logger.trace("Fetching updates with offset $offset")
            val updates = vertx.retrying(shouldStop = { false }) { tg.getUpdates(offset = offset) }
            logger.trace("Received updates $updates")

            val lastOfAll = updates.lastOrNull() ?: continue

            offset = lastOfAll.updateId + 1

            val lastWithDate = updates.lastOrNull { it.message?.date != null }

            if (typedConfig.skipMissing && lastWithDate != null && lastWithDate.message!!.date < startDate) {
                logger.trace("Skipping $updates. These have happened before we have started.")
                continue
            }

            logger.trace("Publishing $updates")
            vertx.eventBus().publishJson(typedConfig.updatePublishingAddress, UpdateList(updates))

        }
    }

    data class Config(
        val tgvAddress: String = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS,
        val updatePublishingAddress: String = WebHook.Config.DEFAULT_UPDATE_PUBLISHING_ADDRESS,
        val skipMissing: Boolean = true
    )
}
