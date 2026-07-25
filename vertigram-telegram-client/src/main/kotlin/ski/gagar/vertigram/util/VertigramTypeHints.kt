package ski.gagar.vertigram.util

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER

/**
 * HTTP payload encoding used by a Telegram callable.
 */
enum class TelegramCallableTransport {
    JSON,
    MULTIPART
}

/**
 * Compile-time-generated metadata describing a concrete Telegram callable.
 *
 * [tgvAddress] is the callable-specific address segment, not the complete event-bus address. `TelegramVerticle`
 * prepends its configured base address and appends the [transport] postfix.
 */
data class TelegramCallableDescriptor(
    val callableClass: Class<*>,
    val telegramMethodName: String,
    val tgvAddress: String,
    val generateVerticleConsumer: Boolean,
    val requestType: JavaType,
    val responseType: JavaType,
    val transport: TelegramCallableTransport
)

/**
 * Type factory from [TELEGRAM_JSON_MAPPER]
 */
val TELEGRAM_TYPE_FACTORY: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

/**
 * Get from map or throw assertion error
 * @param key map key
 */
fun <K, V> Map<K, V>.getOrAssert(key: K) = get(key) ?: throw AssertionError("oops")
