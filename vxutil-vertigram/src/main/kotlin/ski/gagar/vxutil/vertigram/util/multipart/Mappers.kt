package ski.gagar.vxutil.vertigram.util.multipart

import ski.gagar.vxutil.vertigram.util.json.telegramJsonMapper

internal fun telegramJsonMapperWithMultipart() =
    ObjectMapperWithMultipart(telegramJsonMapper())

internal val TELEGRAM_JSON_MAPPER_WITH_MULTIPART = telegramJsonMapperWithMultipart()
