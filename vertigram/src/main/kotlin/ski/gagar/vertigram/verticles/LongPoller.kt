package ski.gagar.vertigram.verticles

import com.fasterxml.jackson.core.type.TypeReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ski.gagar.vertigram.client.Telegram
import ski.gagar.vertigram.client.TgVTelegram
import ski.gagar.vertigram.jackson.typeReference
import ski.gagar.vertigram.lazy
import ski.gagar.vertigram.logger
import ski.gagar.vertigram.methods.deleteWebhook
import ski.gagar.vertigram.retrying
import ski.gagar.vertigram.types.Update
import java.time.Instant

class LongPoller: VertigramVerticle<LongPoller.Config>() {
    override val typeReference: TypeReference<Config> = typeReference()
    private val tg: Telegram by lazy {
        TgVTelegram(vertigram, typedConfig.telegramAddress)
    }

    private var offset: Long? = null
    private lateinit var startDate: Instant

    override suspend fun start() {
        logger.lazy.info { "Deleting old Telegram webhook if any..." }
        startDate = Instant.now()

        retrying(coolDown = { delay(3000) }) {
            tg.deleteWebhook()
        }

        launch {
            doWork()
        }
    }

    private suspend fun doWork() {
        while (true) {
            logger.lazy.trace { "Fetching updates with offset $offset" }
            val updates = try {
                retrying(shouldStop = { false }) {
                    tg.getUpdates(offset = offset, allowedUpdates = typedConfig.allowedUpdates)
                }
            } catch (ex: IllegalArgumentException) {
                // This means we could not even extract update id, so we can't do long poll anymore
                // (since we cannot update offset)
                // We can be more graceful here and forgive these errors if last update has an id,
                // but this probably does not worth it.
                // Hopefully, this will never happen.
                logger.lazy.error { "Got unrecoverable error from getUpdates, giving up long polling" }
                return
            }
            logger.lazy.trace { "Received updates $updates" }

            val lastOfAll = updates.lastOrNull() ?: continue

            offset = lastOfAll.updateId + 1

            val properlyParsed = updates.filterIsInstance<Update.Parsed<*>>()
            val malformed = updates.filterIsInstance<Update.Malformed>()

            if (malformed.isNotEmpty()) {
                logger.lazy.error { "Skipping malformed updates $malformed" }
            }
            val lastWithDate = properlyParsed.lastOrNull { it.date != null }

            if (typedConfig.skipMissing && lastWithDate != null && lastWithDate.date!! < startDate) {
                logger.lazy.trace { "Skipping $properlyParsed. These have happened before we have started." }
                continue
            }

            logger.lazy.trace { "Publishing $properlyParsed" }

            for (u in properlyParsed) {
                vertigram.eventBus.publish(typedConfig.updatePublishingAddress, u)
            }

        }
    }

    data class Config(
        val telegramAddress: String = VertigramAddresses.TELEGRAM_VERTICLE_BASE,
        val updatePublishingAddress: String = VertigramAddresses.UPDATES,
        val skipMissing: Boolean = true,
        val allowedUpdates: List<Update.Type>? = null
    )
}
