package ski.gagar.vertigram.codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.classinspectors.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import ski.gagar.vertigram.annotations.TgMethod
import ski.gagar.vertigram.annotations.TgSuperClass
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
@SupportedAnnotationTypes("ski.gagar.vertigram.annotations.TgMethod", "ski.gagar.vertigram.annotations.TgSuperClass")
class VertigramClientGenerator : AbstractProcessor() {
    private fun logError(msg: String?) {
        processingEnv.messager.printError(msg ?: "Unknown error occured")
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        try {
            return doProcess(annotations, roundEnv)
        } catch (t: Throwable) {
            logError(t.message)
            throw t
        }
    }

    @OptIn(KotlinPoetMetadataPreview::class)
    private fun doProcess(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val ts = roundEnv.getElementsAnnotatedWith(TgMethod::class.java)
            .asSequence()
            .filter { it.kind == ElementKind.CLASS }
            .filter { it.getAnnotation(Metadata::class.java) != null }
            .map { it as TypeElement to it.getAnnotation(TgMethod::class.java) }
            .toList()
        val superClasses = roundEnv.getElementsAnnotatedWith(TgSuperClass::class.java)
            .asSequence()
            .filter { it.kind == ElementKind.CLASS }
            .filter { it.getAnnotation(Metadata::class.java) != null }
            .map { it as TypeElement}
            .toList()
        val inspector = ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)

        val classMetadata = ts.mapNotNull {
            val (te, tgMethod) = it
            val elem = te as? TypeElement ?: return@mapNotNull null
            Triple(te.toTypeSpec(inspector), elem.asClassName(), tgMethod)
        }

        val superClassMetadata = superClasses.map {
            it.asClassName() to it.toTypeSpec(inspector)
        }.toMap()

        if (classMetadata.isEmpty()) return true

        val methods = classMetadata.mapNotNull { (clazz, className, tgMethod) ->
            if (clazz.kind == TypeSpec.Kind.OBJECT) {
                objectToMethod(clazz, className, tgMethod, superClassMetadata)
            } else {
                classToMethod(clazz, className, tgMethod, superClassMetadata)
            }
        }


        val target = File(processingEnv.options["kapt.kotlin.generated"] as String)
        val file = FileSpec.builder("ski.gagar.vertigram.methods", "TgMethods")
            .apply { methods.forEach { addFunction(it) } }
            .build()
        file.writeTo(target)
        return true
    }

    fun objectToMethod(clazz: TypeSpec,
                       className: ClassName,
                       tgMethod: TgMethod?,
                       superClasses: Map<ClassName, TypeSpec>): FunSpec? {
        val returnType = methodReturnType(clazz, superClasses)

        val kotlinMethodName = tgMethod?.kotlinMethodName
        val declName = if (!kotlinMethodName.isNullOrEmpty()) kotlinMethodName else null
        val name = declName
            ?: implicitTgMethodName(clazz)
            ?: return null
        return FunSpec.builder(name)
            .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.client", "Telegram"))
            .returns(returnType)
            .addKdoc("Auto-generated function, please see [%T] docs.".trimIndent(), className)
            .addStatement("return call(%T)", className)
            .build()
    }

    fun classToMethod(clazz: TypeSpec, className: ClassName, tgMethod: TgMethod?, superClasses: Map<ClassName, TypeSpec>): FunSpec? {
        val returnType = methodReturnType(clazz, superClasses)

        val constructor = clazz.primaryConstructor ?: return null
        val kotlinMethodName = tgMethod?.kotlinMethodName
        val declName = if (!kotlinMethodName.isNullOrEmpty()) kotlinMethodName else null
        val name =
            declName
                ?: implicitTgMethodName(clazz)
                ?: return null
        val defaults = clazz.typeSpecs.firstOrNull { it.kind == TypeSpec.Kind.OBJECT && it.name == "Defaults" }

        return FunSpec.builder(name)
            .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.client", "Telegram"))
            .returns(returnType)
            .apply {
                for ((ix, param) in constructor.parameters.withIndex()) {
                    if (param.type == NO_POS_ARGS_TYPE) {
                        check(ix == 0) {
                            "$NO_POS_ARGS_TYPE parameter encountered as non-first parameter for "
                        }
                        continue
                    }
                    addParameter(
                        constructorParamToMethodParam(param, defaults, className)
                    )
                }

                val format = List(parameters.size) {
                    "%N = %N"
                }.joinToString(", ")

                val namedParameters = parameters.asSequence().flatMap { sequenceOf(it, it) }.toList()
                addStatement("return call(%T($format))", className, *namedParameters.toTypedArray())
            }
            .addKdoc("Auto-generated from [%T]".trimIndent(), className)
            .build()
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

    fun methodReturnType(clazz: TypeSpec, superClasses: Map<ClassName, TypeSpec>): TypeName {
        var superclass = clazz.superclass
        var prevSuperclass: TypeName? = null

        var superclassSpec =
            superClasses[superclass.rawIfParametrized()] ?: throw IllegalArgumentException("Wrong superclass")

        while (true) {
            if (superclass is ParameterizedTypeName && superclass.rawType.canonicalName !in SUPERTYPES) {
                throw IllegalStateException("Wrong superclass")
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
                superClasses[superclass.rawIfParametrized()] ?: throw IllegalArgumentException("Wrong superclass")
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

    companion object {
        val SUPERTYPES = setOf(
            "ski.gagar.vertigram.methods.JsonTgCallable",
            "ski.gagar.vertigram.methods.MultipartTgCallable"
        )
        val ROOT_CLASSES = setOf(
            ClassName("java.lang", "Object"),
            ClassName("kotlin", "Any"),
        )

        val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")
    }
}
