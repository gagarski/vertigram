package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

internal fun FunSpec.Builder.addParametersFromPrimaryConstructor(
    classDeclaration: KSClassDeclaration,
    className: ClassName,
    wrappedParameters: MutableMap<String, WrapConfig>,
    wrapRichText: Boolean
) {
    val constructor = classDeclaration.primaryConstructor
        ?: throw IllegalStateException(
            "Cannot add parameters to $this, " +
                    "${classDeclaration.simpleName.getShortName()} has no primary constructor"
        )

    if (Modifier.INTERNAL !in constructor.modifiers) {
        throw IllegalArgumentException("Constructor for $className should be internal")
    }

    val defaults = classDeclaration.declarations
        .filterIsInstance<KSClassDeclaration>()
        .firstOrNull {
            it.classKind == ClassKind.OBJECT &&
                    it.simpleName.getShortName() == DEFAULTS_OBJECT
        }

    addParameter(
        ParameterSpec.builder(NO_POS_ARGS, NO_POS_ARGS_TYPE)
            .defaultValue("ski.gagar.vertigram.util.NoPosArgs.INSTANCE")
            .build()
    )

    val parameterNames = constructor.parameters.mapTo(mutableSetOf(), KSValueParameter::requireName)
    val wrappersBySourceParameter = buildMap {
        for (wrapConfig in WRAP_CONFIGS) {
            if (parameterNames.containsAll(wrapConfig.wrapperParamMapping.keys)) {
                wrapConfig.wrapperParamMapping.keys.forEach { put(it, wrapConfig) }
            }
        }
    }
    val alreadyWrapped = mutableSetOf<String>()

    for (parameter in constructor.parameters) {
        val parameterName = parameter.requireName()
        val wrapConfig = wrappersBySourceParameter[parameterName].takeIf { wrapRichText }

        when {
            parameterName in alreadyWrapped -> continue
            wrapConfig != null -> {
                addParameter(wrapConfig.toParameterSpec(parameter))
                alreadyWrapped.addAll(wrapConfig.wrapperParamMapping.keys)
                wrappedParameters[wrapConfig.wrapperParam] = wrapConfig
            }
            else -> addParameter(parameter.toGeneratedParameter(defaults, className))
        }
    }
}

internal fun FunSpec.Builder.callPrimaryConstructor(
    className: ClassName,
    wrappedParameters: Map<String, WrapConfig>
): FunctionCall {
    val format = parameters
        .asSequence()
        .filterNot { it.name == NO_POS_ARGS }
        .flatMap { parameter ->
            val wrapperConfig = wrappedParameters[parameter.name]
            when {
                wrapperConfig == null -> sequenceOf("%N = %N")
                parameter.type.isNullable ->
                    wrapperConfig.wrapperParamMapping.asSequence().map { "%N = %N?.%N" }
                else ->
                    wrapperConfig.wrapperParamMapping.asSequence().map { "%N = %N.%N" }
            }
        }
        .joinToString(", ")

    val arguments = parameters
        .asSequence()
        .filterNot { it.name == NO_POS_ARGS }
        .flatMap { parameter ->
            val wrapperConfig = wrappedParameters[parameter.name]
            if (wrapperConfig == null) {
                sequenceOf(parameter, parameter)
            } else {
                wrapperConfig.wrapperParamMapping.asSequence().flatMap { (source, target) ->
                    sequenceOf(source, wrapperConfig.wrapperParam, target)
                }
            }
        }

    return FunctionCall(
        formatString = "%T($format)",
        parameters = (sequenceOf(className) + arguments).toList()
    )
}

internal fun Map<String, WrapConfig>.kdocParameterSources(): Map<String, Set<String>> =
    mapValues { (_, config) -> config.wrapperParamMapping.keys }

private fun KSValueParameter.toGeneratedParameter(
    defaults: KSClassDeclaration?,
    className: ClassName
): ParameterSpec {
    val parameterName = requireName()
    return ParameterSpec.builder(parameterName, type.toTypeName())
        .apply {
            if (!hasDefault) return@apply

            val conventionalDefault = defaults
                ?.declarations
                ?.filterIsInstance<KSPropertyDeclaration>()
                ?.firstOrNull { it.simpleName.getShortName() == parameterName }
            when {
                conventionalDefault != null -> defaultValue(
                    "%T.%N.%N",
                    className,
                    defaults.toClassName().simpleName,
                    conventionalDefault.simpleName.getShortName()
                )
                type.resolve().isMarkedNullable -> defaultValue("null")
                type.resolve().rawClassName == BOOLEAN -> defaultValue("false")
                type.resolve().rawClassName == DOUBLE -> defaultValue("0.0")
                type.resolve().rawClassName == NO_POS_ARGS_TYPE ->
                    throw IllegalArgumentException("Please remove NoPosArgs from constructor for $className")
            }
        }
        .build()
}

private fun WrapConfig.toParameterSpec(sourceParameter: KSValueParameter): ParameterSpec {
    val nullable = sourceParameter.type.resolve().isMarkedNullable
    return ParameterSpec.builder(wrapperParam, wrapper.copy(nullable))
        .apply {
            if (nullable) defaultValue("null")
        }
        .build()
}

private fun KSValueParameter.requireName(): String =
    requireNotNull(name) { "Constructor parameters must be named" }.getShortName()

private val KSType.rawClassName: ClassName
    get() = (declaration as KSClassDeclaration).toClassName()

internal data class FunctionCall(
    val formatString: String,
    val parameters: List<Any>
)

internal data class WrapConfig(
    val wrapper: ClassName,
    val wrapperParam: String,
    val wrapperParamMapping: Map<String, String>
)

private const val DEFAULTS_OBJECT = "Defaults"
private const val NO_POS_ARGS = "noPosArgs"
private val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")
private val RICH_TEXT_TYPE = ClassName(
    "ski.gagar.vertigram.telegram.types.richtext",
    "RichText"
)

private val WRAP_CONFIGS = listOf(
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richTitle",
        wrapperParamMapping = mapOf(
            "title" to "text",
            "parseMode" to "parseMode",
            "titleEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richQuestion",
        wrapperParamMapping = mapOf(
            "question" to "text",
            "questionParseMode" to "parseMode",
            "questionEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richDescription",
        wrapperParamMapping = mapOf(
            "description" to "text",
            "descriptionParseMode" to "parseMode",
            "descriptionEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richCaption",
        wrapperParamMapping = mapOf(
            "caption" to "text",
            "parseMode" to "parseMode",
            "captionEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richText",
        wrapperParamMapping = mapOf(
            "text" to "text",
            "parseMode" to "parseMode",
            "entities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richText",
        wrapperParamMapping = mapOf(
            "text" to "text",
            "parseMode" to "parseMode",
            "textEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richMessageText",
        wrapperParamMapping = mapOf(
            "messageText" to "text",
            "parseMode" to "parseMode",
            "entities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richQuote",
        wrapperParamMapping = mapOf(
            "quote" to "text",
            "quoteParseMode" to "parseMode",
            "quoteEntities" to "entities"
        )
    ),
    WrapConfig(
        wrapper = RICH_TEXT_TYPE,
        wrapperParam = "richExplanation",
        wrapperParamMapping = mapOf(
            "explanation" to "text",
            "explanationParseMode" to "parseMode",
            "explanationEntities" to "entities"
        )
    )
)
