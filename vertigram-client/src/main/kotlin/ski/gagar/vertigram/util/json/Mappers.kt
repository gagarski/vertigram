package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinModule

internal fun telegramJsonMapper(): ObjectMapper = ObjectMapper()
    .setAnnotationIntrospector(TelegramAnnotationIntrospector())
    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .registerModule(KotlinModule.Builder().build())
    .registerModule(TelegramModule)

val TELEGRAM_JSON_MAPPER: ObjectMapper =
    telegramJsonMapper()

