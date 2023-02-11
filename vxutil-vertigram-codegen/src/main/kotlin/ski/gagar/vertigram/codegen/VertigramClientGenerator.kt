package ski.gagar.vertigram.codegen

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.classinspectors.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import ski.gagar.vertigram.annotations.TgMethod
import java.io.File
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_19)
@SupportedAnnotationTypes("ski.gagar.vertigram.annotations.TgMethod")
class VertigramClientGenerator : AbstractProcessor() {
    @OptIn(KotlinPoetMetadataPreview::class)
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val ts = roundEnv.getElementsAnnotatedWith(TgMethod::class.java)
            .asSequence()
            .filter { it.kind == ElementKind.CLASS }
            .filter { it.getAnnotation(Metadata::class.java) != null }
            .map { it as TypeElement to it.getAnnotation(TgMethod::class.java) }
            .toList()
        val inspector = ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)

        val classMetadata = ts.mapNotNull {
            val (te, tgMethod) = it
            val elem = te as? TypeElement ?: return@mapNotNull null
            Triple(te.toTypeSpec(inspector), elem.asClassName(), tgMethod)
        }
        if (classMetadata.isEmpty()) return true

        val methods = classMetadata.mapNotNull { (clazz, className, tgMethod) ->
            if (clazz.kind == TypeSpec.Kind.OBJECT) {
                objectToMethod(clazz, className, tgMethod)
            } else {
                classToMethod(clazz, className, tgMethod)
            }
        }


        val target = File(processingEnv.options["kapt.kotlin.generated"] as String)
        val file = FileSpec.builder("ski.gagar.vxutil.vertigram", "TgMethods")
            .apply { methods.forEach { addFunction(it) } }
            .build()
        file.writeTo(target)
        return true
    }

    fun objectToMethod(clazz: TypeSpec, className: ClassName, tgMethod: TgMethod?): FunSpec? {
        val kotlinMethodName = tgMethod?.kotlinMethodName
        val declName = if (!kotlinMethodName.isNullOrEmpty()) kotlinMethodName else null
        val name = declName
            ?: clazz.name?.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            ?: return null
        return FunSpec.builder(name)
            .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vxutil.vertigram.client", "Telegram"))
            .addKdoc("Auto-generated from [%T]".trimIndent(), className)
            .addStatement("return call(%T)", className)
            .build()
    }

    fun classToMethod(clazz: TypeSpec, className: ClassName, tgMethod: TgMethod?): FunSpec? {
        val constructor = clazz.primaryConstructor ?: return null
        val kotlinMethodName = tgMethod?.kotlinMethodName
        val declName = if (!kotlinMethodName.isNullOrEmpty()) kotlinMethodName else null
        val name =
            declName
                ?: clazz.name?.replaceFirstChar { it.lowercase(Locale.getDefault()) }
                ?: return null
        val defaults = clazz.typeSpecs.firstOrNull { it.kind == TypeSpec.Kind.OBJECT && it.name == "Defaults" }

        return FunSpec.builder(name)
            .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vxutil.vertigram.client", "Telegram"))
            .apply {
                for (param in constructor.parameters) {
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

    fun constructorParamToMethodParam(param: ParameterSpec, defaults: TypeSpec?, className: ClassName) =
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
                    }
                }
            }.build()
}
