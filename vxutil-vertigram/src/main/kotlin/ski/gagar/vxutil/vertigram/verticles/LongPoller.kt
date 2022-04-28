package ski.gagar.vxutil.vertigram.verticles

import kotlinx.coroutines.launch
import ski.gagar.vxutil.ErrorLoggingCoroutineVerticle
import ski.gagar.vxutil.jackson.mapTo
import ski.gagar.vxutil.jackson.publishJson
import ski.gagar.vxutil.logger
import ski.gagar.vxutil.retrying
import ski.gagar.vxutil.sleep
import ski.gagar.vxutil.vertigram.client.Telegram
import ski.gagar.vxutil.vertigram.client.TgVTelegram
import ski.gagar.vxutil.vertigram.deleteWebhook
import ski.gagar.vxutil.vertigram.types.MalformedUpdate
import ski.gagar.vxutil.vertigram.types.ParsedUpdate
import ski.gagar.vxutil.vertigram.types.ParsedUpdateList
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
            tg.deleteWebhook()
        }

        launch {
            doWork()
        }
    }

    private suspend fun doWork() {
        while (true) {
            logger.trace("Fetching updates with offset $offset")
            val updates = try {
                vertx.retrying(shouldStop = { false }) { tg.getUpdates(offset = offset) }
            } catch (ex: IllegalArgumentException) {
                // This means we could not even extract update id, so we can't do long poll anymore
                // (since we cannot update offset)
                // We can be more graceful here and forgive these errors if last update has an id,
                // but this probably does not worth it.
                // Hopefully, this will never happen.
                logger.error("Got unrecoverable error from getUpdates, giving up long polling")
                return
            }
            logger.trace("Received updates $updates")

            val lastOfAll = updates.lastOrNull() ?: continue

            offset = lastOfAll.updateId + 1

            val properlyParsed = updates.filterIsInstance<ParsedUpdate>()
            val malformed = updates.filterIsInstance<MalformedUpdate>()

            if (malformed.isNotEmpty()) {
                logger.error("Skipping malformed updates $malformed")
            }
            val lastWithDate = properlyParsed.lastOrNull { it.message?.date != null }

            if (typedConfig.skipMissing && lastWithDate != null && lastWithDate.message!!.date < startDate) {
                logger.trace("Skipping $properlyParsed. These have happened before we have started.")
                continue
            }

            logger.trace("Publishing $properlyParsed")
            vertx.eventBus().publishJson(typedConfig.updatePublishingAddress, ParsedUpdateList(properlyParsed))

        }
    }

    data class Config(
        val tgvAddress: String = TelegramVerticle.Config.DEFAULT_BASE_ADDRESS,
        val updatePublishingAddress: String = WebHook.Config.DEFAULT_UPDATE_PUBLISHING_ADDRESS,
        val skipMissing: Boolean = true
    )
}
