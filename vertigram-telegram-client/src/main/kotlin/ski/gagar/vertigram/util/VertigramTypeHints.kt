package ski.gagar.vertigram.util

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory
import org.apache.commons.lang3.StringUtils
import org.reflections.Reflections
import ski.gagar.vertigram.telegram.annotations.TelegramMethod
import ski.gagar.vertigram.telegram.methods.JsonTelegramCallable
import ski.gagar.vertigram.telegram.methods.MultipartTelegramCallable
import ski.gagar.vertigram.telegram.methods.TelegramCallable
import ski.gagar.vertigram.uncheckedCast
import ski.gagar.vertigram.uncheckedCastOrNull
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import java.lang.reflect.GenericDeclaration
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

/**
 * Type factory from [TELEGRAM_JSON_MAPPER]
 */
val TELEGRAM_TYPE_FACTORY: TypeFactory = TELEGRAM_JSON_MAPPER.typeFactory

/**
 * Response type for `this`.
 *
 * Deduced by generic parameters passed to [TelegramCallable].
 */
private val <T> Class<T>.responseType: JavaType
    get() {
        if (typeParameters.isNotEmpty())
            throw IllegalArgumentException("$this should not have type parameters")

        var current: Class<*>? = this
        var params: Map<TypeVariable<*>, Type> = mapOf()

        while (current != null) {
            val type = current.genericSuperclass
            if (type !is ParameterizedType) {
                current = current.superclass
                continue
            }

            val rawType = type.rawType

            if (rawType == TelegramCallable::class.java) {
                val t = type.actualTypeArguments[0]
                val tInst = if (t is Class<*>) {
                    t
                } else {
                    params[t as TypeVariable<*>]
                }
                return TELEGRAM_TYPE_FACTORY.constructType(tInst)
            }

            params = rawType
                .uncheckedCast<GenericDeclaration>()
                .typeParameters
                .zip(type.actualTypeArguments)
                .associate { (k, v) ->
                    val vInst =
                        when (v) {
                            is TypeVariable<*> -> {
                                params[v] ?: throw AssertionError("oops")
                            }
                            is Class<*>, is ParameterizedType -> {
                                v
                            }
                            else -> {
                                throw AssertionError("oops")
                            }
                        }
                    k to vInst
                }
            current = rawType.uncheckedCastOrNull<Class<*>>()
        }

        throw IllegalArgumentException("$this is not a subtype of TgCallable")
}

/**
 * List of classes, representing Telegram method.
 */
private fun getTgCallables() =
    Reflections("ski.gagar.vertigram.telegram.methods")
        .getTypesAnnotatedWith(ski.gagar.vertigram.telegram.annotations.TelegramMethod::class.java, true)
        .asSequence()
        .filter { !Modifier.isAbstract(it.modifiers) }
        .toSet()

/**
 * Get consumer address for `TelegramVerticle`
 */
private val <T> Class<T>.tgvAddress: String
    get() = getAnnotation(ski.gagar.vertigram.telegram.annotations.TelegramMethod::class.java).verticleConsumerName.ifEmpty { name.uncapitalizeDotSeparated() }

/**
 * Do [StringUtils.uncapitalize] on each part of string, separated by dot.
 */
private fun String.uncapitalizeDotSeparated() =
    split('.').map { StringUtils.uncapitalize(it) }.joinToString(".")

/**
 * Get method name passed to Telegram.
 */
private val <T> Class<T>.telegramMethodName: String
    get() = getAnnotation(ski.gagar.vertigram.telegram.annotations.TelegramMethod::class.java).methodName.ifEmpty { StringUtils.uncapitalize(simpleName) }

/**
 * Type hints used by Vertigram implementation to deserialize Telegram entities.
 */
object VertigramTypeHints {
    /**
     * Set of Telegram callable classes
     */
    val callables = getTgCallables()

    /**
     * [callables] associated with [telegramMethodName]
     */
    val methodNameByClass = callables.associateWith {
        it.telegramMethodName
    }

    /**
     * [callables] associated with [tgvAddress]
     */
    val tgvAddressByClass = callables.associateWith {
        it.tgvAddress
    }

    /**
     * Set of Telegram methods, for which the consumer is not generated in `TelegramVerticle`
     */
    val doNotGenerateInTgVerticleAddresses =
        tgvAddressByClass
            .filter { (k, _) -> k.getAnnotation(ski.gagar.vertigram.telegram.annotations.TelegramMethod::class.java)?.generateVerticleConsumer == false }
            .values
            .toSet()

    /**
     * [callables] associated with [responseType]
     */
    val responseTypeByClass = callables.associateWith {
        it.responseType
    }

    /**
     * Type hints for JSON callables
     */
    object Json {
        /**
         * Set of JSON Telegram callables
         */
        private val callables = VertigramTypeHints.callables.filter {
            JsonTelegramCallable::class.java.isAssignableFrom(it)
        }

        /**
         * Map of [tgvAddress] to request type for JSON Telegram callables
         */
        val requestTypeByTgvAddress = callables.associate {
            it.tgvAddress to TELEGRAM_TYPE_FACTORY.constructType(it)
        }
    }

    /**
     * Type hints for multipart callables
     */
    object Multipart {
        /**
         * Set of Multipart Telegram callables
         */
        private val callables = VertigramTypeHints.callables.filter {
            MultipartTelegramCallable::class.java.isAssignableFrom(it)
        }

        /**
         * Map of [tgvAddress] to request type for Multipart Telegram callables
         */
        val requestTypeByTgvAddress = callables.associate {
            it.tgvAddress to TELEGRAM_TYPE_FACTORY.constructType(it)
        }
    }
}

/**
 * Get from map or throw assertion error
 * @param key map key
 */
fun <K, V> Map<K, V>.getOrAssert(key: K) = get(key) ?: throw AssertionError("oops")
