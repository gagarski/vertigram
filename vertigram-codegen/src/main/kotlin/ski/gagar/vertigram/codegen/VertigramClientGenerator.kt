package ski.gagar.vertigram.codegen

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
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
                addParametersFromPrimaryConstructor(clazz, className)
                val call = callPrimaryConstructor(className)
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
                addParametersFromPrimaryConstructor(clazz, className)
                val call = callPrimaryConstructor(className)
                addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
            }
            .addAutoGeneratedKdoc(className)
            .build()
    }

    private fun FunSpec.Builder.addParametersFromPrimaryConstructor(
        clazz: TypeSpec,
        className: ClassName
    ) {
        val constructor = clazz.primaryConstructor
            ?: throw IllegalStateException("Cannot add parameters to ${this}, ${clazz.name} has no primary constructor")

        val defaults = clazz.typeSpecs.firstOrNull { it.kind == TypeSpec.Kind.OBJECT && it.name == "Defaults" }


        for ((ix, param) in constructor.parameters.withIndex()) {
            if (param.type == NO_POS_ARGS_TYPE) {
                check(ix == 0) {
                    "$NO_POS_ARGS_TYPE parameter encountered as non-first parameter for "
                }
                continue
            }
            this.addParameter(
                constructorParamToMethodParam(param, defaults, className)
            )
        }
    }

    private fun FunSpec.Builder.callPrimaryConstructor(
        className: ClassName
    ): FunctionCall {
        val format = List(parameters.size) {
            "%N = %N"
        }.joinToString(", ")

        val namedParameters = parameters.asSequence().flatMap { sequenceOf(it, it) }

        return FunctionCall(
            formatString = "%T($format)",
            parameters = (sequenceOf(className) + namedParameters).toList()
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

    companion object {
        val SUPERTYPES = setOf(
            "ski.gagar.vertigram.methods.JsonTelegramCallable",
            "ski.gagar.vertigram.methods.MultipartTelegramCallable"
        )
        val ROOT_CLASSES = setOf(
            ClassName("java.lang", "Object"),
            ClassName("kotlin", "Any"),
        )

        val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")
        const val TG_METHODS = "TelegramMethods"
        const val TG_CONSTRUCTORS = "TelegramConstructors"
    }
}
