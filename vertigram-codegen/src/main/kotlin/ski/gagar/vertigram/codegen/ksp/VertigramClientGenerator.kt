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
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val builders = mutableMapOf<FileSpecBuilderKey, FileSpec.Builder>()
        val methods = resolver.getMethodsToGenerate()
        for (clazz in methods.values) {
            clazz.processMethod(builders, methods)
        }

        val types = resolver.getTypesToGenerate()


        for (clazz in types.values) {
            clazz.processType(builders, types)
        }

        for (bld in builders.values) {
            bld.build().writeTo(codeGenerator, Dependencies(true))
        }

        return emptyList()
    }

    private fun TypeInfoMethod.processMethod(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>,
        typeInfos: Map<ClassName, TypeInfoMethod>
    ) {
        val (className, classDecl, annotation) = this

        val methodsFile = fileSpecBuilders.computeIfAbsent(
            FileSpecBuilderKey(
                METHODS_PACKAGE,
                TG_METHODS
            )
        ) {
            FileSpec.builder(METHODS_PACKAGE, TG_METHODS)
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
                methodsFile.addFunction(method)
            }
            else -> throw IllegalStateException("$className has a kind ${classDecl.classKind} which is not supported")

        }
    }

    private fun TypeInfoType.processType(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>,
        typeInfos: Map<ClassName, TypeInfoType>
    ) {
        val (className, classDecl, annotation) = this

        val creatorsFile = fileSpecBuilders.computeIfAbsent(
            FileSpecBuilderKey(
                className.packageName,
                TG_CREATORS
            )
        ) {
            FileSpec.builder(className.packageName, TG_CREATORS)
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
            ClassKind.CLASS -> {
                val creator = creator(classDecl, className, annotation)

                creatorsFile.addFunction(creator)

                val constructor = creator(classDecl, className, annotation, INVOKE, true)

                constructorsFile.addFunction(constructor)
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
                        throw IllegalArgumentException("Please remove NoPosArgs from constructor for $className")
                    }
                }
            }.build()

    private fun FunSpec.Builder.addParametersFromPrimaryConstructor(
        classDecl: KSClassDeclaration,
        className: ClassName,
        actuallyWrapped: MutableSet<String>,
        wrapRichText: Boolean
    ) {
        val constructor = classDecl.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, " +
                    "${classDecl.simpleName.getShortName()} has no primary constructor")

        if (!constructor.modifiers.contains(Modifier.INTERNAL))
            throw IllegalArgumentException("Constructor for $className should be internal")

        val defaults = classDecl.declarations
            .firstOrNull {
                it is KSClassDeclaration &&
                        it.classKind == ClassKind.OBJECT &&
                        it.simpleName.getShortName() == "Defaults"
            } as? KSClassDeclaration

        val seenParams = mutableSetOf<String>()

        val alreadyWrapped = mutableSetOf<String>()

        this.addParameter(
            ParameterSpec.builder(NO_POS_ARGS, NO_POS_ARGS_TYPE)
                .defaultValue("ski.gagar.vertigram.util.NoPosArgs.INSTANCE")
                .build()
        )

        for (param in constructor.parameters) {
            val wrapConfig = if (wrapRichText) WRAP_CONFIGS_BY_TRIGGER[param.name!!.getShortName()] else null

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
                if (param.name == NO_POS_ARGS) continue
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
                if (param.name == NO_POS_ARGS) continue
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
             
             For up-to-date documentation, please consult the official Telegram docs.
             
             This function is auto-generated from [%T]
        """.trimIndent(), from)
    }

    private fun getNames(
        classDecl: KSClassDeclaration,
        anno: TelegramCodegen.Method): Names {
        val kotlinMethodName = anno.name
        val declName = kotlinMethodName.ifEmpty { null }
        val name = declName
            ?: implicitTgMethodName(classDecl)

        val telegramName = anno.telegramName.ifEmpty { null }
            ?: name
        return Names(name, telegramName)
    }

    private fun kotlinMethodForClass(
        classDecl: KSClassDeclaration,
        className: ClassName,
        anno: TelegramCodegen.Method,
        typeInfo: Map<ClassName, TypeInfoMethod>
    ): FunSpec {
        val returnType = methodReturnType(classDecl, typeInfo)

        val names = getNames(classDecl, anno)

        return FunSpec.builder(names.methodName)
            .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"DEPRECATION\"").build())
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.telegram.client", "Telegram"))
            .returns(returnType)
            .apply {
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno.wrapRichText)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return call(${call.formatString})", *call.parameters.toTypedArray())
            }
            .addMethodKdoc(names.telegramName, className)
            .build()
    }

    private fun kotlinMethodForObject(classDecl: KSClassDeclaration,
                                      className: ClassName,
                                      anno: TelegramCodegen.Method,
                                      typeInfos: Map<ClassName, TypeInfoMethod>): FunSpec? {
        val returnType = methodReturnType(classDecl, typeInfos)
        val names = getNames(classDecl, anno)

        return FunSpec.builder(names.methodName)
            .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"DEPRECATION\"").build())
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

    private fun methodReturnType(clazz: KSClassDeclaration, typeInfo: Map<ClassName, TypeInfoMethod>): TypeName {
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

    private fun creator(
        classDecl: KSClassDeclaration,
        className: ClassName,
        anno: TelegramCodegen.Type,
        name: String = CREATE,
        isOperator: Boolean = false
    ): FunSpec {
        val receiver = sequence {
            yieldAll(className.simpleNames)
            yield("Companion")
        }.toList()

        classDecl.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.isCompanionObject }
            .firstOrNull() ?: throw IllegalArgumentException("$className should have a companion object")

        return FunSpec.builder(name)
            .returns(className)
            .receiver(ClassName(className.packageName, receiver))
            .apply {

                if (isOperator) {
                    addModifiers(KModifier.OPERATOR)
                }
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno.wrapRichText)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
            }
            .addAutoGeneratedKdoc(className)
            .build()
    }

    @OptIn(KspExperimental::class)
    private fun Resolver.getMethodsToGenerate(): Map<ClassName, TypeInfoMethod> {
        val types = this.getSymbolsWithAnnotation("ski.gagar.vertigram.annotations.TelegramCodegen.Method")
            .filterIsInstance(KSClassDeclaration::class.java)
            .filter { it.validate() }
            .map { it to it.getAnnotationsByType(TelegramCodegen.Method::class).first() }
            .toList()
        return types.asSequence().map { (classDecl, anno) ->
            val className = classDecl.toClassName()
            className to TypeInfoMethod(className, classDecl, anno)
        }.toMap()
    }

    @OptIn(KspExperimental::class)
    private fun Resolver.getTypesToGenerate(): Map<ClassName, TypeInfoType> {
        val types = this.getSymbolsWithAnnotation("ski.gagar.vertigram.annotations.TelegramCodegen.Type")
            .filterIsInstance(KSClassDeclaration::class.java)
            .filter { it.validate() }
            .map { it to it.getAnnotationsByType(TelegramCodegen.Type::class).first() }
            .toList()
        return types.asSequence().map { (classDecl, anno) ->
            val className = classDecl.toClassName()
            className to TypeInfoType(className, classDecl, anno)
        }.toMap()
    }

    data class TypeInfoMethod(val className: ClassName, val classDecl: KSClassDeclaration, val annotation: TelegramCodegen.Method)
    data class TypeInfoType(val className: ClassName, val classDecl: KSClassDeclaration, val annotation: TelegramCodegen.Type)
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
        private const val TG_CREATORS = "TelegramCreators"
        private const val TG_CONSTRUCTORS = "TelegramConstructors"
        private val SUPERTYPES = setOf(
            "ski.gagar.vertigram.telegram.types.methods.JsonTelegramCallable",
            "ski.gagar.vertigram.telegram.types.methods.MultipartTelegramCallable"
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
                triggerParam = "messageText",
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richMessageText",
                wrapperParamMapping = mapOf(
                    "messageText" to "text",
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
        private const val NO_POS_ARGS = "noPosArgs"
        private const val METHODS_PACKAGE = "ski.gagar.vertigram.telegram.methods"
        private const val CREATE = "create"
        private const val INVOKE = "invoke"
    }
}