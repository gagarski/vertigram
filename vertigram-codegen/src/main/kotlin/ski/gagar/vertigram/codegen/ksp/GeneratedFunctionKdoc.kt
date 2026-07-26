package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec

internal fun FunSpec.Builder.addGeneratedKdoc(
    classDeclaration: KSClassDeclaration,
    className: ClassName,
    wrappedParameterSources: Map<String, Set<String>> = emptyMap(),
    telegramName: String? = null
) = apply {
    val sourceKdoc = classDeclaration.mergedKdoc()
    if (sourceKdoc.isNotBlank()) {
        addKdoc("%L\n\n", sourceKdoc)
        addKdoc("Generated from [%T].", className)

        val parameterTags = generatedParameterKdocs(
            wrappedParameterSources,
            classDeclaration.constructorParameterKdocs(),
            parameters.map(ParameterSpec::name)
        )
        if (parameterTags.isNotBlank()) {
            addKdoc("\n\n%L\n", parameterTags)
        }
        return@apply
    }

    if (telegramName != null) {
        addKdoc(
            "Telegram [%L](https://core.telegram.org/bots/api#%L) method.\n\n" +
                    "For up-to-date documentation, please consult the official Telegram docs.\n\n" +
                    "This function is auto-generated from [%T].\n",
            telegramName,
            telegramName.lowercase(),
            className
        )
        return@apply
    }

    addKdoc("Auto-generated function, please see [%T] docs.", className)
}

private fun KSClassDeclaration.mergedKdoc(): String =
    generateSequence(this as KSDeclaration?) { it.parentDeclaration }
        .filterIsInstance<KSClassDeclaration>()
        .toList()
        .asReversed()
        .mapNotNull(KSClassDeclaration::docString)
        .map { it.normalizeKdoc() }
        .filter(String::isNotBlank)
        .distinct()
        .joinToString("\n\n")

private fun String.normalizeKdoc(): String =
    lineSequence()
        .map { line ->
            line.trim()
                .removePrefix("/**")
                .removeSuffix("*/")
                .removePrefix("*")
                .trimStart()
        }
        .dropWhile(String::isBlank)
        .toList()
        .dropLastWhile(String::isBlank)
        .joinToString("\n")

private fun generatedParameterKdocs(
    wrappedParameterSources: Map<String, Set<String>>,
    constructorParameterKdocs: Map<String, String>,
    generatedParameterNames: List<String>
): String {
    val wrapperDocs = wrappedParameterSources.mapValues { (_, sourceParameterNames) ->
        val sourceDescriptions = sourceParameterNames
            .mapNotNull(constructorParameterKdocs::get)
            .distinct()
        buildString {
                append("Formatted-text value supplying ")
            append(sourceParameterNames.joinToString { "`$it`" })
            append(" to the Telegram request.")
            if (sourceDescriptions.isNotEmpty()) {
                append(' ')
                append(sourceDescriptions.joinToString(" "))
            }
        }
    }

    return generatedParameterNames
        .mapNotNull { parameterName ->
            if (parameterName == NO_POS_ARGS) return@mapNotNull null
            val documentation = wrapperDocs[parameterName]
                ?: constructorParameterKdocs[parameterName]
                ?: return@mapNotNull null
            wrapParameterKdoc(parameterName, documentation)
        }
        .joinToString("\n")
}

private fun wrapParameterKdoc(parameterName: String, documentation: String): String {
    val firstLinePrefix = "@param $parameterName "
    val words = documentation.split(WHITESPACE)
    val lines = mutableListOf<String>()
    var prefix = firstLinePrefix
    var currentLine = StringBuilder(prefix)

    for (word in words) {
        val separatorLength = if (currentLine.length == prefix.length) 0 else 1
        if (
            currentLine.length > prefix.length &&
            currentLine.length + separatorLength + word.length > KDOC_CONTENT_WIDTH
        ) {
            lines += currentLine.toString()
            prefix = KDOC_TAG_CONTINUATION
            currentLine = StringBuilder(prefix)
        }
        if (currentLine.length > prefix.length) currentLine.append(' ')
        currentLine.append(word)
    }
    lines += currentLine.toString()
    return lines.joinToString("\n")
}

private fun KSClassDeclaration.constructorParameterKdocs(): Map<String, String> =
    declarations
        .filterIsInstance<KSPropertyDeclaration>()
        .filter { property ->
            primaryConstructor?.parameters?.any {
                it.name?.getShortName() == property.simpleName.getShortName()
            } == true
        }
        .mapNotNull { property ->
            val documentation = property.docString
                ?.normalizeKdoc()
                ?.replace(WHITESPACE, " ")
                ?.trim()
                ?.takeIf(String::isNotBlank)
                ?: return@mapNotNull null
            property.simpleName.getShortName() to documentation
        }
        .toMap()

private const val NO_POS_ARGS = "noPosArgs"
private const val KDOC_CONTENT_WIDTH = 117
private const val KDOC_TAG_CONTINUATION = "    "
private val WHITESPACE = Regex("""\s+""")
