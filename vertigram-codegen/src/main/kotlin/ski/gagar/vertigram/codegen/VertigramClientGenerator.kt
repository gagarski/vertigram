package ski.gagar.vertigram.codegen

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.classinspectors.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import ski.gagar.vertigram.annotations.TelegramCodegen
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("ski.gagar.vertigram.annotations.TelegramCodegen")
class VertigramClientGenerator : AbstractProcessor() {
    private fun logError(msg: String?) {
        processingEnv.messager.printError(msg ?: "Unknown error occured")
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        try {
            doProcess(roundEnv)
            return true
        } catch (t: Throwable) {
            logError(t.message)
            throw t
        }
    }

    private fun doProcess(roundEnv: RoundEnvironment) {
        val classes = roundEnv.getProcessedClasses()

        val builders = mutableMapOf<FileSpecBuilderKey, FileSpec.Builder>()

        for (clazz in classes.values) {
            clazz.process(builders, classes)
        }


        val target = File(processingEnv.options["kapt.kotlin.generated"] as String)
        for (bld in builders.values) {
            bld.build().writeTo(target)
        }
    }

    @OptIn(KotlinPoetMetadataPreview::class, DelicateKotlinPoetApi::class)
    private fun RoundEnvironment.getProcessedClasses(): Map<ClassName, TypeInfo> {
        val inspector = ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)

        val types = getElementsAnnotatedWith(TelegramCodegen::class.java)
            .asSequence()
            .filter { it.kind == ElementKind.CLASS }
            .filter { it.getAnnotation(Metadata::class.java) != null }
            .map { it as TypeElement to it.getAnnotation(TelegramCodegen::class.java) }
            .toList()

        return types.asSequence().map { (typeElement, anno) ->
            val typeSpec = typeElement.toTypeSpec(inspector)
            val className = typeElement.asClassName()
            className to TypeInfo(className, typeSpec, anno)
        }.toMap()
    }

    private fun TypeInfo.process(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>,
        typeInfos: Map<ClassName, TypeInfo>
    ) {
        val (className, typeSpec, annotation) = this

        val methodsFile = fileSpecBuilders.computeIfAbsent(FileSpecBuilderKey(className.packageName, TG_METHODS)) {
            FileSpec.builder(className.packageName, TG_METHODS)
        }
        val constructorsFile = fileSpecBuilders.computeIfAbsent(FileSpecBuilderKey(className.packageName, TG_CONSTRUCTORS)) {
            FileSpec.builder(className.packageName, TG_CONSTRUCTORS)
        }

        val richTextWrappersFile = fileSpecBuilders.computeIfAbsent(FileSpecBuilderKey(className.packageName, TG_RICH_TEXT_WRAPPERS)) {
            FileSpec.builder(className.packageName, TG_RICH_TEXT_WRAPPERS)
        }

        when (typeSpec.kind) {
            TypeSpec.Kind.OBJECT -> {
                val method = kotlinMethodForObject(typeSpec, className, annotation, typeInfos)
                method?.let { methodsFile.addFunction(it) }
            }
            TypeSpec.Kind.CLASS -> {
                if (typeSpec.modifiers.contains(KModifier.ABSTRACT) || typeSpec.modifiers.contains(KModifier.SEALED))
                    return
                val method = kotlinMethodForClass(typeSpec, className, annotation, typeInfos)
                method?.let { methodsFile.addFunction(it) }

                val constructor = pseudoConstructor(typeSpec, className, annotation)

                constructor?.let { constructorsFile.addFunction(it) }

                val richTextWrappers = richTextWrappers(typeSpec, className, annotation)

                for (w in richTextWrappers) {
                    richTextWrappersFile.addProperty(w)
                }
            }
            else -> throw IllegalStateException("$className has a kind ${typeSpec.kind} which is not supported")

        }
    }

    private fun kotlinMethodForObject(clazz: TypeSpec,
                                      className: ClassName,
                                      anno: TelegramCodegen,
                                      typeInfos: Map<ClassName, TypeInfo>): FunSpec? {
        if (!anno.generateMethod)
            return null
        val returnType = methodReturnType(clazz, typeInfos)

        val kotlinMethodName = anno.methodName
        val declName = kotlinMethodName.ifEmpty { null }
        val name = declName
            ?: implicitTgMethodName(clazz)
            ?: throw IllegalStateException("Was not able to detect method name for $className. Is it anonymous?")
        return FunSpec.builder(name)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.client", "Telegram"))
            .returns(returnType)
            .addAutoGeneratedKdoc(className)
            .addStatement("return call(%T)", className)
            .build()
    }

    private fun kotlinMethodForClass(
        clazz: TypeSpec,
        className: ClassName,
        anno: TelegramCodegen,
        typeInfo: Map<ClassName, TypeInfo>
    ): FunSpec? {
        if (!anno.generateMethod)
            return null

        val returnType = methodReturnType(clazz, typeInfo)

        val kotlinMethodName = anno.methodName
        val declName = kotlinMethodName.ifEmpty { null }
        val name = declName
            ?: implicitTgMethodName(clazz)
            ?: throw IllegalStateException("Was not able to detect method name for $className. Is it anonymous?")

        return FunSpec.builder(name)
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.client", "Telegram"))
            .returns(returnType)
            .apply {
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(clazz, className, actuallyWrapped, anno)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return call(${call.formatString})", *call.parameters.toTypedArray())
            }
            .addAutoGeneratedKdoc(className)
            .build()
    }

    private fun FunSpec.Builder.addAutoGeneratedKdoc(className: ClassName) = apply {
        addKdoc("Auto-generated function, please see [%T] docs.", className)
    }

    private fun pseudoConstructor(
        clazz: TypeSpec,
        className: ClassName,
        anno: TelegramCodegen
    ): FunSpec? {
        if (!anno.generatePseudoConstructor)
            return null

        val constructorName = anno.pseudoConstructorName
        val declName = constructorName.ifEmpty { null }
        val name = declName
            ?: clazz.name
            ?: throw IllegalStateException("Was not able to detect method name for $className. Is it anonymous?")

        val constructor = clazz.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, ${clazz.name} has no primary constructor")

        if (KModifier.INTERNAL !in constructor.modifiers) {
            throw IllegalStateException("In order to generate pseudoConstructor for ${className}, " +
                    "primary constructor has to be declared as internal")
        }
        return FunSpec.builder(name)
            .returns(className)
            .apply {
                val actuallyWrapped = mutableSetOf<String>()
                addParametersFromPrimaryConstructor(clazz, className, actuallyWrapped, anno)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
            }
            .addAutoGeneratedKdoc(className)
            .build()
    }

    private fun richTextWrappers(
        clazz: TypeSpec,
        className: ClassName,
        anno: TelegramCodegen
    ): List<PropertySpec> {
        if (!anno.generateRichTextWrappers)
            return listOf()

        val constructorName = anno.pseudoConstructorName
        val declName = constructorName.ifEmpty { null }
        val name = declName
            ?: clazz.name
            ?: throw IllegalStateException("Was not able to detect method name for $className. Is it anonymous?")

        val constructor = clazz.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, ${clazz.name} has no primary constructor")



        val wrappers = sequence {
            for (param in constructor.parameters) {
                val wrapperConf = WRAP_CONFIGS_BY_TRIGGER[param.name] ?: continue

                val argsFormat = sequence {
                    for ((_, _) in wrapperConf.wrapperParamMapping) {
                        yield("%N = %N")
                    }
                }.joinToString(", ")

                val args = sequence {
                    for ((from, to) in wrapperConf.wrapperParamMapping) {
                        yield(to)
                        yield(from)
                    }
                }.toList().toTypedArray()

                val propSpec = if (!param.type.isNullable) {
                    PropertySpec.builder(wrapperConf.wrapperParam, wrapperConf.wrapper)
                        .receiver(className)
                        .getter(
                            FunSpec.getterBuilder()
                                .addStatement("""
                                    return %T($argsFormat)
                                    """.trimIndent(),
                                    wrapperConf.wrapper,
                                    *args
                                ).build()
                        )
                        .build()
                } else {
                    PropertySpec.builder(wrapperConf.wrapperParam, wrapperConf.wrapper.copy(true))
                        .receiver(className)
                        .getter(
                            FunSpec.getterBuilder()
                                .addStatement("""
                                    %N ?: return null 
                                """.trimIndent(), wrapperConf.triggerParam)
                                .addStatement("""
                                    return %T($argsFormat)
                                    """.trimIndent(),
                                    wrapperConf.wrapper,
                                    *args
                                ).build()
                        )
                        .build()
                }

                yield(propSpec)
            }
        }

        return wrappers.toList()
    }

    private fun FunSpec.Builder.addParametersFromPrimaryConstructor(
        clazz: TypeSpec,
        className: ClassName,
        actuallyWrapped: MutableSet<String>,
        anno: TelegramCodegen
    ) {
        val constructor = clazz.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, ${clazz.name} has no primary constructor")

        val defaults = clazz.typeSpecs.firstOrNull { it.kind == TypeSpec.Kind.OBJECT && it.name == "Defaults" }

        val seenParams = mutableSetOf<String>()

        val alreadyWrapped = mutableSetOf<String>()

        for (param in constructor.parameters) {
            val wrapConfig = if (anno.generateRichTextWrappers) WRAP_CONFIGS_BY_TRIGGER[param.name] else null

            if (param.name in alreadyWrapped) {
                continue
            } else if (null != wrapConfig) {
                val wrapperType =
                    if (param.type.isNullable)
                        wrapConfig.wrapper.copy(true)
                    else
                        wrapConfig.wrapper

                this.addParameter(
                    ParameterSpec.builder(wrapConfig.wrapperParam, wrapperType).apply {
                        if (param.type.isNullable) {
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
            seenParams.add(param.name)
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
                val wrapperConfig = if (param.name in actuallyWrapped) WRAP_CONFIGS_BY_WRAPPER[param.name] else null

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
                val wrapperConfig = if (param.name in actuallyWrapped) WRAP_CONFIGS_BY_WRAPPER[param.name] else null

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

    private fun constructorParamToMethodParam(param: ParameterSpec, defaults: TypeSpec?, className: ClassName) =
        ParameterSpec.builder(param.name, param.type)
            .apply {
                if (null != param.defaultValue) {
                    val conventionalDefault = defaults?.propertySpecs?.firstOrNull { it.name == param.name }
                    if (null != conventionalDefault) {
                        defaultValue("%T.%N.%N", className, defaults, conventionalDefault)
                    } else if (param.type.isNullable) {
                        defaultValue("null")
                    } else if (param.type == BOOLEAN) {
                        defaultValue("false")
                    } else if (param.type == ClassName("ski.gagar.vertigram.util", "NoPosArgs")) {
                        defaultValue("ski.gagar.vertigram.util.NoPosArgs.INSTANCE")
                    }
                }
            }.build()

    private fun implicitTgMethodName(clazz: TypeSpec) =
        clazz.name?.replaceFirstChar { it.lowercase(Locale.getDefault()) }

    fun methodReturnType(clazz: TypeSpec, typeInfo: Map<ClassName, TypeInfo>): TypeName {
        var superclass = clazz.superclass
        var prevSuperclass: TypeName? = null

        var superclassSpec =
            typeInfo[superclass.rawIfParametrized()]?.typeSpec ?: throw IllegalArgumentException("Wrong superclass $superclass (no annotation)")

        while (true) {
            if (superclass is ParameterizedTypeName && superclass.rawType.canonicalName !in SUPERTYPES) {
                throw IllegalStateException("Intermediate parametrized classes are not supported $superclass")
            }

            if (superclass.rawIfParametrized().canonicalName in SUPERTYPES) {
                break
            }

            if (superclass in ROOT_CLASSES || superclass == prevSuperclass) {
                throw IllegalStateException("Wrong superclass")
            }

            prevSuperclass = superclass
            superclass = superclassSpec.superclass

            superclassSpec =
                typeInfo[superclass.rawIfParametrized()]?.typeSpec ?: throw IllegalArgumentException("Wrong superclass")
        }

        if (superclass !is ParameterizedTypeName || superclass.rawType.canonicalName !in SUPERTYPES)
            throw IllegalArgumentException("$clazz is not a proper tg method")

        return superclass.typeArguments.firstOrNull() ?: throw IllegalArgumentException("$clazz is not a proper tg method")
    }

    private fun TypeName.rawIfParametrized(): ClassName {
        if (this is ParameterizedTypeName) {
            return rawType
        } else if (this is ClassName) {
            return this
        } else {
            throw IllegalArgumentException("Wrong type")
        }
    }

    data class TypeInfo(val className: ClassName, val typeSpec: TypeSpec, val annotation: TelegramCodegen)
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

    companion object {
        private val SUPERTYPES = setOf(
            "ski.gagar.vertigram.methods.JsonTelegramCallable",
            "ski.gagar.vertigram.methods.MultipartTelegramCallable"
        )
        private val ROOT_CLASSES = setOf(
            ClassName("java.lang", "Object"),
            ClassName("kotlin", "Any"),
        )

        private val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")
        private const val TG_METHODS = "TelegramMethods"
        private const val TG_CONSTRUCTORS = "TelegramConstructors"
        private const val TG_RICH_TEXT_WRAPPERS = "TelegramRichTextWrappers"

        // TODO: Remove that when https://github.com/FasterXML/jackson-databind/issues/1467 is implemented
        //       and replace with @JsonUnwrapped
        private val WRAP_CONFIGS = listOf(
            WrapConfig(
                triggerParam = "caption",
                wrapper = ClassName("ski.gagar.vertigram.richtext", "RichCaption"),
                wrapperParam = "richCaption",
                wrapperParamMapping = mapOf(
                    "caption" to "caption",
                    "parseMode" to "parseMode",
                    "captionEntities" to "captionEntities"
                )
            ),
            WrapConfig(
                triggerParam = "text",
                wrapper = ClassName("ski.gagar.vertigram.richtext", "RichText"),
                wrapperParam = "richText",
                wrapperParamMapping = mapOf(
                    "text" to "text",
                    "parseMode" to "parseMode",
                    "entities" to "entities"
                )
            ),
            WrapConfig(
                triggerParam = "quote",
                wrapper = ClassName("ski.gagar.vertigram.richtext", "RichQuote"),
                wrapperParam = "richQuote",
                wrapperParamMapping = mapOf(
                    "quote" to "quote",
                    "quoteParseMode" to "quoteParseMode",
                    "quoteEntities" to "quoteEntities"
                )
            ),
            WrapConfig(
                triggerParam = "explanation",
                wrapper = ClassName("ski.gagar.vertigram.richtext", "RichExplanation"),
                wrapperParam = "richExplanation",
                wrapperParamMapping = mapOf(
                    "explanation" to "explanation",
                    "explanationParseMode" to "explanationParseMode",
                    "explanationEntities" to "explanationEntities"
                )
            ),
        )

        private val WRAP_CONFIGS_BY_TRIGGER = WRAP_CONFIGS.associateBy { it.triggerParam }
        private val WRAP_CONFIGS_BY_WRAPPER = WRAP_CONFIGS.associateBy { it.wrapperParam }
    }
}
