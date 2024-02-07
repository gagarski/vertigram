package ski.gagar.vertigram.util

import com.fasterxml.jackson.databind.JavaType
import org.apache.commons.lang3.StringUtils
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import ski.gagar.vertigram.methods.JsonTgCallable
import ski.gagar.vertigram.methods.MultipartTgCallable
import ski.gagar.vertigram.methods.TgCallable
import ski.gagar.vertigram.uncheckedCast
import ski.gagar.vertigram.uncheckedCastOrNull
import ski.gagar.vertigram.util.json.TELEGRAM_JSON_MAPPER
import java.lang.reflect.*


private val TYPE_FACTORY = TELEGRAM_JSON_MAPPER.typeFactory

private val <T: TgCallable<*>> Class<T>.responseType: JavaType
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

            if (rawType == TgCallable::class.java) {
                val t = type.actualTypeArguments[0]
                val tInst = if (t is Class<*>) {
                    t
                } else {
                    params[t as TypeVariable<*>]
                }
                return TYPE_FACTORY.constructType(tInst)
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

private fun getTgCallables() =
    Reflections("ski.gagar.vertigram.methods", Scanners.SubTypes)
        .getSubTypesOf(TgCallable::class.java)
        .asSequence()
        .filter { !Modifier.isAbstract(it.modifiers) }
        .toSet()

private val <T: TgCallable<*>> Class<T>.tgvAddress: String?
    get() = getAnnotation(TgVerticleGenerate::class.java)?.address?.let {
        if (it == DEFAULT_ADDRESS) {
            null
        } else {
            it
        }
    }

private val <T: TgCallable<*>> Class<T>.tgMethodName: String
    get() = getAnnotation(TgMethodName::class.java)?.name ?: StringUtils.uncapitalize(simpleName)

object VertigramTypeHints {
    val callables = getTgCallables()

    val methodNames = callables.associateWith {
        it.tgMethodName
    }

    val tgvAddresses = callables.associateWith {
        it.tgvAddress ?: it.tgMethodName
    }

    val doNotGenerateInTgVerticleAddresses =
        tgvAddresses
            .filter { (k, _) -> k.getAnnotation(TgVerticleGenerate::class.java)?.generate == false }
            .values
            .toSet()

    val returnTypesByClass = callables.associateWith {
        it.responseType
    }

    object Json {
        private val callables = VertigramTypeHints.callables.filter {
            JsonTgCallable::class.java.isAssignableFrom(it)
        }

        val requestTypesByTgvAddress = callables.associate {
            (it.tgvAddress ?: it.tgMethodName) to TYPE_FACTORY.constructType(it)
        }

        val returnTypesByTgvAddress = callables.associate {
            (it.tgvAddress ?: it.tgMethodName) to it.responseType
        }

    }

    object Multipart {
        private val callables = VertigramTypeHints.callables.filter {
            MultipartTgCallable::class.java.isAssignableFrom(it)
        }

        val requestTypesByTgvAddress = callables.associate {
            (it.tgvAddress ?: it.tgMethodName) to TYPE_FACTORY.constructType(it)
        }

        val returnTypesByTgvAddress = callables.associate {
            (it.tgvAddress ?: it.tgMethodName) to it.responseType
        }

    }
}

fun <K, V> Map<K, V>.getOrAssert(key: K) = get(key) ?: throw AssertionError("oops")
