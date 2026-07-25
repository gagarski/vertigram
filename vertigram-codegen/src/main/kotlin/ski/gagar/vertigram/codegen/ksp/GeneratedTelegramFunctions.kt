package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import ski.gagar.vertigram.annotations.TelegramCodegen

internal fun TypeInfoMethod.addClientMethodTo(
    fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>
) {
    if (!annotation.generateClientMethod) return

    val resolvedCallableType = callableType
    if (resolvedCallableType == null) {
        when (classDecl.classKind) {
            ClassKind.INTERFACE -> return
            ClassKind.CLASS -> {
                if (Modifier.ABSTRACT in classDecl.modifiers || Modifier.SEALED in classDecl.modifiers) return
            }
            else -> {}
        }
        throw IllegalStateException("$className has a kind ${classDecl.classKind} which is not supported")
    }

    val methodsFile = fileSpecBuilders.getOrPut(
        FileSpecBuilderKey(METHODS_PACKAGE, TG_METHODS)
    ) {
        FileSpec.builder(METHODS_PACKAGE, TG_METHODS)
    }

    val method = when (classDecl.classKind) {
        ClassKind.OBJECT -> clientMethodForObject(
            classDecl,
            className,
            annotation,
            resolvedCallableType.returnType
        )
        ClassKind.CLASS -> clientMethodForClass(
            classDecl,
            className,
            annotation,
            resolvedCallableType.returnType
        )
        else -> error("Concrete callable $className has an unsupported kind ${classDecl.classKind}")
    }
    methodsFile.addFunction(method)
}

internal fun TypeInfoType.addCreatorFunctionsTo(
    fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>
) {
    if (classDecl.classKind != ClassKind.CLASS) {
        throw IllegalStateException("$className has a kind ${classDecl.classKind} which is not supported")
    }

    val creatorsFile = fileSpecBuilders.getOrPut(
        FileSpecBuilderKey(className.packageName, TG_CREATORS)
    ) {
        FileSpec.builder(className.packageName, TG_CREATORS)
    }
    val constructorsFile = fileSpecBuilders.getOrPut(
        FileSpecBuilderKey(className.packageName, TG_CONSTRUCTORS)
    ) {
        FileSpec.builder(className.packageName, TG_CONSTRUCTORS)
    }

    creatorsFile.addFunction(
        creatorFunction(classDecl, className, annotation)
    )
    constructorsFile.addFunction(
        creatorFunction(classDecl, className, annotation, INVOKE, isOperator = true)
    )
}

private fun clientMethodForClass(
    classDeclaration: KSClassDeclaration,
    className: ClassName,
    annotation: TelegramCodegen.Method,
    returnType: TypeName
): FunSpec {
    val names = telegramMethodNames(classDeclaration, annotation)
    val wrappedParameters = mutableMapOf<String, WrapConfig>()

    return FunSpec.builder(names.methodName)
        .addAnnotation(DEPRECATION_SUPPRESSION)
        .addModifiers(KModifier.SUSPEND)
        .receiver(TELEGRAM_TYPE)
        .returns(returnType)
        .apply {
            addParametersFromPrimaryConstructor(
                classDeclaration,
                className,
                wrappedParameters,
                annotation.wrapRichText
            )
            val call = callPrimaryConstructor(className, wrappedParameters)
            addStatement("return call(${call.formatString})", *call.parameters.toTypedArray())
        }
        .addGeneratedKdoc(
            classDeclaration,
            className,
            wrappedParameters.kdocParameterSources(),
            names.telegramName
        )
        .build()
}

private fun clientMethodForObject(
    classDeclaration: KSClassDeclaration,
    className: ClassName,
    annotation: TelegramCodegen.Method,
    returnType: TypeName
): FunSpec {
    val names = telegramMethodNames(classDeclaration, annotation)

    return FunSpec.builder(names.methodName)
        .addAnnotation(DEPRECATION_SUPPRESSION)
        .addModifiers(KModifier.SUSPEND)
        .receiver(TELEGRAM_TYPE)
        .returns(returnType)
        .addGeneratedKdoc(
            classDeclaration,
            className,
            telegramName = names.telegramName
        )
        .addStatement("return call(%T)", className)
        .build()
}

private fun creatorFunction(
    classDeclaration: KSClassDeclaration,
    className: ClassName,
    annotation: TelegramCodegen.Type,
    name: String = CREATE,
    isOperator: Boolean = false
): FunSpec {
    val companionReceiver = ClassName(
        className.packageName,
        className.simpleNames + "Companion"
    )
    classDeclaration.declarations
        .filterIsInstance<KSClassDeclaration>()
        .firstOrNull(KSClassDeclaration::isCompanionObject)
        ?: throw IllegalArgumentException("$className should have a companion object")

    val wrappedParameters = mutableMapOf<String, WrapConfig>()
    return FunSpec.builder(name)
        .returns(className)
        .receiver(companionReceiver)
        .apply {
            if (isOperator) addModifiers(KModifier.OPERATOR)
            addParametersFromPrimaryConstructor(
                classDeclaration,
                className,
                wrappedParameters,
                annotation.wrapRichText
            )
            val call = callPrimaryConstructor(className, wrappedParameters)
            addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
        }
        .addGeneratedKdoc(
            classDeclaration,
            className,
            wrappedParameters.kdocParameterSources()
        )
        .build()
}

internal data class FileSpecBuilderKey(
    val packageName: String,
    val fileName: String
)

private val TELEGRAM_TYPE = ClassName(
    "ski.gagar.vertigram.telegram.client",
    "Telegram"
)
private val DEPRECATION_SUPPRESSION = AnnotationSpec.builder(Suppress::class)
    .addMember("\"DEPRECATION\"")
    .build()
private const val METHODS_PACKAGE = "ski.gagar.vertigram.telegram.methods"
private const val TG_METHODS = "TelegramMethods"
private const val TG_CREATORS = "TelegramCreators"
private const val TG_CONSTRUCTORS = "TelegramConstructors"
private const val CREATE = "create"
private const val INVOKE = "invoke"
