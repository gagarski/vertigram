package ski.gagar.vertigram.util

import com.fasterxml.jackson.databind.JavaType
import org.apache.commons.lang3.StringUtils
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import ski.gagar.vertigram.entities.requests.JsonTgCallable
import ski.gagar.vertigram.entities.requests.MultipartTgCallable
import ski.gagar.vertigram.entities.requests.TgCallable
import ski.gagar.vxutil.uncheckedCast
import ski.gagar.vxutil.uncheckedCastOrNull
import java.lang.reflect.*


private const val MULTIPART = "Multipart"
private val TYPE_FACTORY = TELEGRAM_JSON_MAPPER.typeFactory

private fun String.dropSuffix(suffix: String) =
    if (endsWith(suffix))
        substring(0, lastIndexOf(suffix))
    else
        this

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
                .map { (k, v) ->
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
                .toMap()
            current = rawType.uncheckedCastOrNull<Class<*>>()
        }

        throw IllegalArgumentException("$this is not a subtype of TgCallable")
}

private fun getTgCallables() =
    Reflections("ski.gagar.vertigram.entities.requests", SubTypesScanner())
        .getSubTypesOf(TgCallable::class.java)
        .asSequence()
        .filter { !Modifier.isAbstract(it.modifiers) }
        .toSet()



private val <T: TgCallable<*>> Class<T>.tgMethodName: String
    get() = getAnnotation(TgMethodName::class.java)?.name ?: StringUtils.uncapitalize(simpleName.dropSuffix(
        MULTIPART
    ))


internal object TypeHints {
    val callables = getTgCallables()

    val methodNames = callables.map {
        it to it.tgMethodName
    }.toMap()

    val doNotGenerateInTgVerticleMethodNames =
        methodNames
            .filter { (k, _) -> k.getAnnotation(DoNotGenerateInTgVerticle::class.java) != null }
            .values
            .toSet()

    val returnTypesByClass = callables.map {
        it to it.responseType
    }.toMap()

    object Json {
        private val callables = TypeHints.callables.filter {
            JsonTgCallable::class.java.isAssignableFrom(it)
        }

        val requestTypesByMethodName = callables.map {
            it.tgMethodName to TYPE_FACTORY.constructType(it)
        }.toMap()

        val returnTypesByMethodName = callables.map {
            it.tgMethodName to it.responseType
        }.toMap()

    }

    object Multipart {
        private val callables = TypeHints.callables.filter {
            MultipartTgCallable::class.java.isAssignableFrom(it)
        }

        val requestTypesByMethodName = callables.map {
            it.tgMethodName to TYPE_FACTORY.constructType(it)
        }.toMap()

        val returnTypesByMethodName = callables.map {
            it.tgMethodName to it.responseType
        }.toMap()

    }

}

internal fun <K, V> Map<K, V>.getOrAssert(key: K) = get(key) ?: throw AssertionError("oops")
