package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * Create [TELEGRAM_JSON_MAPPER]
 */
internal fun telegramJsonMapper(): ObjectMapper = ObjectMapper()
    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(KotlinModule.Builder().build())
    .registerModule(TelegramModule)

/**
 * JSON mapper for Telegram.
 *
 * Key features:
 *  - snake_case notation
 *  - do not include null fields
 *  - do not fail on unknown properties
 *  - use [KotlinModule]
 *  - use [TelegramModule] to implement Telegram-specific serialization formats
 */
val TELEGRAM_JSON_MAPPER: ObjectMapper =
    telegramJsonMapper()

