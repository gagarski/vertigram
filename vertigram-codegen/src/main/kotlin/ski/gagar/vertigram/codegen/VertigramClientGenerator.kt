package ski.gagar.vertigram.codegen

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.classinspector.elements.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import ski.gagar.vertigram.annotations.TgMethod
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_16)
@SupportedOptions(VertigramClientGenerator.VERTIGRAM_GENERATED_CODE_PATH)
@SupportedAnnotationTypes("ski.gagar.vertigram.annotations.TgMethod")
class VertigramClientGenerator : AbstractProcessor() {
    companion object {
        const val VERTIGRAM_GENERATED_CODE_PATH = "vertigram.generated"
    }

    @KotlinPoetMetadataPreview
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment): Boolean {
        val ts = roundEnv.getElementsAnnotatedWith(TgMethod::class.java)
            .asSequence()
            .filter { it.kind == ElementKind.CLASS }
            .filter{ it.getAnnotation(Metadata::class.java) != null }
            .map { it as TypeElement }
            .toList()
        val inspector = ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)

        Thread.currentThread().contextClassLoader = javaClass.classLoader

        val classMetadata = ts.mapNotNull {
            val elem = it as? TypeElement ?: return@mapNotNull null
            elem.toTypeSpec(inspector)
        }

        classMetadata.forEach {
            println(it.primaryConstructor?.parameters?.map { it.defaultValue })
        }
        return true
    }
}
