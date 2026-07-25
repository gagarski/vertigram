package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

internal class VertigramTypeHintsGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {
    fun generate(methods: Collection<TypeInfoMethod>) {
        if (methods.isEmpty()) return

        val callables = methods
            .asSequence()
            .filter { it.callableType != null }
            .sortedBy { it.className.canonicalName }
            .toList()
        validateUniqueTgvAddresses(callables)

        val classStar = ClassName("java.lang", "Class").parameterizedBy(STAR)
        val descriptorClass = ClassName(TYPE_HINTS_PACKAGE, CALLABLE_DESCRIPTOR)
        val transportClass = ClassName(TYPE_HINTS_PACKAGE, CALLABLE_TRANSPORT)

        val typeHints = TypeSpec.objectBuilder(TYPE_HINTS_OBJECT)
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "DEPRECATION")
                    .build()
            )
            .addKdoc("Type hints used by Vertigram to dispatch and deserialize Telegram methods.\n")
            .addProperty(
                PropertySpec.builder(
                    "descriptorByCallable",
                    MAP.parameterizedBy(classStar, descriptorClass)
                )
                    .addKdoc("Concrete Telegram callable classes associated with their descriptors.\n")
                    .initializer(
                        CodeBlock.builder()
                            .add("mapOf<%T, %T>(\n", classStar, descriptorClass)
                            .indent()
                            .apply {
                                callables.forEach {
                                    add(
                                        "%T::class.java to %L,\n",
                                        it.className,
                                        it.descriptorCodeBlock(descriptorClass, transportClass)
                                    )
                                }
                            }
                            .unindent()
                            .add(")")
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder(
                    "descriptorByTgvAddress",
                    MAP.parameterizedBy(STRING, descriptorClass)
                )
                    .addKdoc("Descriptors indexed by callable-specific address segment, derived at runtime.\n")
                    .initializer("descriptorByCallable.values.associateBy(%T::tgvAddress)", descriptorClass)
                    .build()
            )

        FileSpec.builder(TYPE_HINTS_PACKAGE, GENERATED_TYPE_HINTS)
            .addType(typeHints.build())
            .build()
            .writeTo(
                codeGenerator,
                Dependencies(
                    aggregating = true,
                    *methods.mapNotNull { it.classDecl.containingFile }.distinct().toTypedArray()
                )
            )
    }

    private fun validateUniqueTgvAddresses(callables: Collection<TypeInfoMethod>) {
        val collisions = callables
            .groupBy { it.tgvAddress }
            .filterValues { it.size > 1 }
        if (collisions.isEmpty()) return

        collisions.forEach { (address, methods) ->
            val classes = methods.joinToString { it.className.canonicalName }
            methods.forEach {
                logger.error("Duplicate Vertigram address '$address' used by $classes", it.classDecl)
            }
        }
        throw IllegalStateException(
            "Telegram code generation failed: ${collisions.size} duplicate Vertigram address(es)"
        )
    }
}

private fun TypeInfoMethod.descriptorCodeBlock(
    descriptorClass: ClassName,
    transportClass: ClassName
): CodeBlock {
    val callableType = requireNotNull(callableType)
    return CodeBlock.builder()
        .add("%T(\n", descriptorClass)
        .indent()
        .add("callableClass = %T::class.java,\n", className)
        .add("telegramMethodName = %S,\n", telegramMethodNames(classDecl, annotation).telegramName)
        .add("tgvAddress = %S,\n", tgvAddress)
        .add("generateVerticleConsumer = %L,\n", annotation.generateVerticleConsumer)
        .add(
            "requestType = TELEGRAM_TYPE_FACTORY.constructType(%T::class.java),\n",
            className
        )
        .add("responseType = %L,\n", callableType.returnType.javaTypeCodeBlock())
        .add("transport = %T.%L,\n", transportClass, callableType.transport.name)
        .unindent()
        .add(")")
        .build()
}

private fun TypeName.javaTypeCodeBlock(): CodeBlock =
    if (this is ClassName) {
        CodeBlock.of(
            "TELEGRAM_TYPE_FACTORY.constructType(%T::class.%M)",
            this,
            JAVA_OBJECT_TYPE
        )
    } else {
        val typeReference = ClassName(
            "com.fasterxml.jackson.core.type",
            "TypeReference"
        ).parameterizedBy(this)
        CodeBlock.of(
            "TELEGRAM_TYPE_FACTORY.constructType(object : %T() {}.type)",
            typeReference
        )
    }

private val TypeInfoMethod.tgvAddress: String
    get() = annotation.verticleConsumerName.ifEmpty {
        className.simpleNames
            .joinToString(".") { it.replaceFirstChar(Char::lowercaseChar) }
    }

private val JAVA_OBJECT_TYPE = MemberName("kotlin.jvm", "javaObjectType")
private const val TYPE_HINTS_PACKAGE = "ski.gagar.vertigram.util"
private const val GENERATED_TYPE_HINTS = "GeneratedVertigramTypeHints"
private const val TYPE_HINTS_OBJECT = "VertigramTypeHints"
private const val CALLABLE_DESCRIPTOR = "TelegramCallableDescriptor"
private const val CALLABLE_TRANSPORT = "TelegramCallableTransport"
