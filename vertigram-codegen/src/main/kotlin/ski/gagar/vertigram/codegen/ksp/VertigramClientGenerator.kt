package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.util.*

class VertigramClientGenerator(
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classes = resolver.getProcessedClasses()
        val builders = mutableMapOf<FileSpecBuilderKey, FileSpec.Builder>()

        for (clazz in classes.values) {
            clazz.process(builders, classes)
        }

        for (bld in builders.values) {
            bld.build().writeTo(codeGenerator, Dependencies(true))
        }

        return emptyList()
    }

    private fun TypeInfo.process(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>,
        typeInfos: Map<ClassName, TypeInfo>
    ) {
        val (className, classDecl, annotation) = this

        val methodsFile = fileSpecBuilders.computeIfAbsent(
            FileSpecBuilderKey(
                className.packageName,
                TG_METHODS
            )
        ) {
            FileSpec.builder(className.packageName, TG_METHODS)
        }

        val constructorsFile = fileSpecBuilders.computeIfAbsent(
            FileSpecBuilderKey(
                className.packageName,
                TG_CONSTRUCTORS
            )
        ) {
            FileSpec.builder(className.packageName, TG_CONSTRUCTORS)
        }

        when (classDecl.classKind) {
            ClassKind.OBJECT -> {
                val method = kotlinMethodForObject(classDecl, className, annotation, typeInfos)
                method?.let { methodsFile.addFunction(it) }
            }
            ClassKind.INTERFACE -> {}
            ClassKind.CLASS -> {
                if (classDecl.modifiers.contains(Modifier.ABSTRACT) || classDecl.modifiers.contains(Modifier.SEALED))
                    return
                val method = kotlinMethodForClass(classDecl, className, annotation, typeInfos)
                method?.let { methodsFile.addFunction(it) }

                val constructor = pseudoConstructor(classDecl, className, annotation)

                constructor?.let { constructorsFile.addFunction(it) }

            }
            else -> throw IllegalStateException("$className has a kind ${classDecl.classKind} which is not supported")

        }
    }

    private fun implicitTgMethodName(classDecl: KSClassDeclaration) =
        classDecl.simpleName.getShortName().replaceFirstChar { it.lowercase(Locale.getDefault()) }

    private fun constructorParamToMethodParam(param: KSValueParameter,
                                              defaults: KSClassDeclaration?, className: ClassName) =
        ParameterSpec.builder(param.name!!.getShortName(), param.type.toTypeName())
            .apply {
                if (param.hasDefault) {
                    val conventionalDefault = defaults?.declarations?.firstOrNull {
                        it is KSPropertyDeclaration &&
                        it.simpleName.getShortName() == param.name!!.getShortName()
                    }
                    if (null != conventionalDefault) {
                        defaultValue("%T.%N.%N", className, defaults.toClassName().simpleName, conventionalDefault.simpleName.getShortName())
                    } else if (param.type.resolve().isMarkedNullable) {
                        defaultValue("null")
                    } else if (param.type.resolve().rawClassName == BOOLEAN) {
                        defaultValue("false")
                    } else if (param.type.resolve().rawClassName == ClassName("ski.gagar.vertigram.util", "NoPosArgs")) {
                        addAnnotation(AnnotationSpec.builder(ClassName("kotlin", "Suppress")).addMember("\"UNUSED_PARAMETER\"").build())
                        defaultValue("ski.gagar.vertigram.util.NoPosArgs.INSTANCE")
                    }
                }
            }.build()

    private fun FunSpec.Builder.addParametersFromPrimaryConstructor(
        classDecl: KSClassDeclaration,
        className: ClassName,
        actuallyWrapped: MutableSet<String>,
        anno: TelegramCodegen
    ) {
        val constructor = classDecl.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, " +
                    "${classDecl.simpleName.getShortName()} has no primary constructor")

        val defaults = classDecl.declarations
            .firstOrNull {
                it is KSClassDeclaration &&
                        it.classKind == ClassKind.OBJECT &&
                        it.simpleName.getShortName() == "Defaults"
            } as? KSClassDeclaration

        val seenParams = mutableSetOf<String>()

        val alreadyWrapped = mutableSetOf<String>()

        for (param in constructor.parameters) {
            val wrapConfig = if (anno.wrapRichText) WRAP_CONFIGS_BY_TRIGGER[param.name!!.getShortName()] else null

            if (param.name!!.getShortName() in alreadyWrapped) {
                continue
            } else if (null != wrapConfig) {
                val wrapperType =
                    if (param.type.resolve().isMarkedNullable)
                        wrapConfig.wrapper.copy(true)
                    else
                        wrapConfig.wrapper

                this.addParameter(
                    ParameterSpec.builder(wrapConfig.wrapperParam, wrapperType).apply {
                        if (param.type.resolve().isMarkedNullable) {
                            defaultValue("null")
                        }
                    }.build()
                )

                if (wrapConfig.wrapperParamMapping.any { (k, _) -> k in seenParams }) {
                    throw IllegalStateException("Some of the parameters in ${wrapConfig.wrapperParamMapping} are already processed")
                }
                alreadyWrapped.addAll(wrapConfig.wrapperParamMapping.keys)
                actuallyWrapped.add(wrapConfig.wrapperParam)
            } else {
                this.addParameter(
                    constructorParamToMethodParam(param, defaults, className)
                )
            }
            seenParams.add(param.name!!.getShortName())
        }
    }

    private fun FunSpec.Builder.callPrimaryConstructor(
        className: ClassName,
        actuallyWrapped: Set<String>
    ): FunctionCall {
        val format = sequence {
            for (param in parameters) {
                if (param.type == NO_POS_ARGS_TYPE) {
                    continue
                }
                val wrapperConfig =
                    if (param.name in actuallyWrapped)
                        WRAP_CONFIGS_BY_WRAPPER[param.name] else null

                if (null == wrapperConfig)
                    yield("%N = %N")
                else {
                    for ((_, _) in wrapperConfig.wrapperParamMapping) {
                        if (param.type.isNullable) {
                            yield("%N = %N?.%N")
                        } else {
                            yield("%N = %N.%N")
                        }
                    }
                }
            }
        }.joinToString(", ")

        val params = sequence {
            for (param in parameters) {
                if (param.type == NO_POS_ARGS_TYPE) {
                    continue
                }
                val wrapperConfig =
                    if (param.name in actuallyWrapped)
                        WRAP_CONFIGS_BY_WRAPPER[param.name] else null

                if (null == wrapperConfig) {
                    yield(param)
                    yield(param)
                } else {
                    for ((from, to) in wrapperConfig.wrapperParamMapping) {
                        yield(from)
                        yield(wrapperConfig.wrapperParam)
                        yield(to)
                    }
                }


            }
        }

        return FunctionCall(
            formatString = "%T($format)",
            parameters = (sequenceOf(className) + params).toList()
        )
    }

    private fun FunSpec.Builder.addMethodKdoc(telegramName: String, from: ClassName) = apply {
        addKdoc("""
             Telegram [${telegramName}](https://core.telegram.org/bots/api#${telegramName.lowercase()}) method.
             
             For up-to-date documentation please consult the official Telegram docs.
             
             This function is auto-generated from [%T]
        """.trimIndent(), from)
    }

    private fun getNames(
        classDecl: KSClassDeclaration,
        anno: TelegramCodegen): Names {
        val kotlinMethodName = anno.methodName
        val declName = kotlinMethodName.ifEmpty { null }
        val name = declName
            ?: implicitTgMethodName(classDecl)

        val telegramName = anno.docMethodName.ifEmpty { null }
            ?: name
        return Names(name, telegramName)
    }

    private fun kotlinMethodForClass(
        classDecl: KSClassDeclaration,
        className: ClassName,
        anno: TelegramCodegen,
        typeInfo: Map<ClassName, TypeInfo>
    ): FunSpec? {
        if (!anno.generateMethod)
            return null
        val returnType = methodReturnType(classDecl, typeInfo)

        val names = getNames(classDecl, anno)

        return FunSpec.builder(names.methodName)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.telegram.client", "Telegram"))
            .returns(returnType)
            .apply {
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return call(${call.formatString})", *call.parameters.toTypedArray())
            }
            .addMethodKdoc(names.telegramName, className)
            .build()
    }

    private fun kotlinMethodForObject(classDecl: KSClassDeclaration,
                                      className: ClassName,
                                      anno: TelegramCodegen,
                                      typeInfos: Map<ClassName, TypeInfo>): FunSpec? {
        if (!anno.generateMethod)
            return null
        val returnType = methodReturnType(classDecl, typeInfos)
        val names = getNames(classDecl, anno)


        return FunSpec.builder(names.methodName)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.telegram.client", "Telegram"))
            .returns(returnType)
            .addMethodKdoc(names.telegramName, className)
            .addStatement("return call(%T)", className)
            .build()
    }

    private val KSClassDeclaration.superClass
        get() = superTypes
            .map { it.resolve() }
            .filter {
                val decl = it.declaration
                decl is KSClassDeclaration && decl.classKind == ClassKind.CLASS }
            .first()

    private val KSType.rawClassName: ClassName
        get() = (declaration as KSClassDeclaration).toClassName()

    private fun methodReturnType(clazz: KSClassDeclaration, typeInfo: Map<ClassName, TypeInfo>): TypeName {
        var superclass = clazz.superClass
        var prevSuperclass: KSType? = null
        var superclassDecl =
            typeInfo[superclass.rawClassName]?.classDecl ?: throw IllegalArgumentException("Wrong superclass $superclass (no annotation)")

        while (true) {
            if (superclass.arguments.isNotEmpty() && superclass.rawClassName.canonicalName !in SUPERTYPES) {
                throw IllegalStateException("Intermediate parametrized classes are not supported $superclass")
            }

            if (superclass.rawClassName.canonicalName in SUPERTYPES) {
                break
            }

            if (superclass.rawClassName in ROOT_CLASSES || superclass == prevSuperclass) {
                throw IllegalStateException("Wrong superclass")
            }

            prevSuperclass = superclass
            superclass = superclassDecl.superClass

            superclassDecl =
                typeInfo[superclass.rawClassName]?.classDecl ?: throw IllegalArgumentException("Wrong superclass")
        }

        if (superclass.arguments.isEmpty()  || superclass.rawClassName.canonicalName !in SUPERTYPES)
            throw IllegalArgumentException("$clazz is not a proper tg method")

        return superclass.arguments.firstOrNull()?.toTypeName() ?: throw IllegalArgumentException("$clazz is not a proper tg method")
    }

    private fun FunSpec.Builder.addAutoGeneratedKdoc(className: ClassName) = apply {
        addKdoc("Auto-generated function, please see [%T] docs.", className)
    }

    private fun pseudoConstructor(
        classDecl: KSClassDeclaration,
        className: ClassName,
        anno: TelegramCodegen
    ): FunSpec? {
        if (!anno.generatePseudoConstructor)
            return null

        val consName = anno.pseudoConstructorName.ifEmpty { null }

        val name = when {
            null != consName -> consName
            className.simpleNames.size == 1 -> className.simpleName
            else -> "invoke"
        }

        val receiver = when {
            null != consName -> null
            className.simpleNames.size == 1 -> null
            else -> sequence {
                yieldAll(className.simpleNames)
                yield("Companion")
            }.toList()
        }

        val constructor = classDecl.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, ${classDecl.toClassName().simpleName} has no primary constructor")

        if (Modifier.INTERNAL !in constructor.modifiers) {
            throw IllegalStateException("In order to generate pseudoConstructor for ${className}, " +
                    "primary constructor has to be declared as internal")
        }
        return FunSpec.builder(name)
            .returns(className)
            .apply {
                receiver?.let {
                    addModifiers(KModifier.OPERATOR)
                    receiver(ClassName(className.packageName, it))
                }
            }
            .apply {
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
            }
            .addAutoGeneratedKdoc(className)
            .build()
    }

    @OptIn(KspExperimental::class)
    private fun Resolver.getProcessedClasses(): Map<ClassName, TypeInfo> {
        val types = this.getSymbolsWithAnnotation("ski.gagar.vertigram.annotations.TelegramCodegen")
            .filterIsInstance(KSClassDeclaration::class.java)
            .filter { it.validate() }
            .map { it to it.getAnnotationsByType(TelegramCodegen::class).first() }
            .toList()
        return types.asSequence().map { (classDecl, anno) ->
            val className = classDecl.toClassName()
            className to TypeInfo(className, classDecl, anno)
        }.toMap()
    }

    data class TypeInfo(val className: ClassName, val classDecl: KSClassDeclaration, val annotation: TelegramCodegen)
    data class FileSpecBuilderKey(val packageName: String, val fileName: String)

    data class FunctionCall(
        val formatString: String,
        val parameters: List<Any>
    )

    data class WrapConfig(
        val triggerParam: String,
        val wrapper: ClassName,
        val wrapperParam: String,
        val wrapperParamMapping: Map<String, String>
    )

    data class Names(val methodName: String, val telegramName: String)

    companion object {
        private const val TG_METHODS = "TelegramMethods"
        private const val TG_CONSTRUCTORS = "TelegramConstructors"
        private val SUPERTYPES = setOf(
            "ski.gagar.vertigram.telegram.methods.JsonTelegramCallable",
            "ski.gagar.vertigram.telegram.methods.MultipartTelegramCallable"
        )
        private val ROOT_CLASSES = setOf(
            ClassName("java.lang", "Object"),
            ClassName("kotlin", "Any"),
        )
        private val WRAP_CONFIGS = listOf(
            WrapConfig(
                triggerParam = "caption",
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richCaption",
                wrapperParamMapping = mapOf(
                    "caption" to "text",
                    "parseMode" to "parseMode",
                    "captionEntities" to "entities"
                )
            ),
            WrapConfig(
                triggerParam = "text",
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richText",
                wrapperParamMapping = mapOf(
                    "text" to "text",
                    "parseMode" to "parseMode",
                    "entities" to "entities"
                )
            ),
            WrapConfig(
                triggerParam = "quote",
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richQuote",
                wrapperParamMapping = mapOf(
                    "quote" to "text",
                    "quoteParseMode" to "parseMode",
                    "quoteEntities" to "entities"
                )
            ),
            WrapConfig(
                triggerParam = "explanation",
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richExplanation",
                wrapperParamMapping = mapOf(
                    "explanation" to "text",
                    "explanationParseMode" to "parseMode",
                    "explanationEntities" to "entities"
                )
            ),
        )

        private val WRAP_CONFIGS_BY_TRIGGER = WRAP_CONFIGS.associateBy { it.triggerParam }
        private val WRAP_CONFIGS_BY_WRAPPER = WRAP_CONFIGS.associateBy { it.wrapperParam }
        private val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")

    }
}