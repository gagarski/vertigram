package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import ski.gagar.vertigram.annotations.TelegramCodegen

internal fun Resolver.telegramMethodDeclarations(): List<KSClassDeclaration> =
    classDeclarationsAnnotatedWith(METHOD_ANNOTATION)

internal fun Resolver.telegramTypeDeclarations(): List<KSClassDeclaration> =
    classDeclarationsAnnotatedWith(TYPE_ANNOTATION)

@OptIn(KspExperimental::class)
internal fun Iterable<KSClassDeclaration>.toMethodInfos(): Map<ClassName, TypeInfoMethod> =
    asSequence()
        .map { it to it.getAnnotationsByType(TelegramCodegen.Method::class).first() }
        .associate { (classDeclaration, annotation) ->
            val className = classDeclaration.toClassName()
            className to TypeInfoMethod(
                className,
                classDeclaration,
                annotation,
                classDeclaration.takeIf(KSClassDeclaration::isConcreteCallable)
                    ?.let(::resolveCallableType)
            )
        }

@OptIn(KspExperimental::class)
internal fun Iterable<KSClassDeclaration>.toTypeInfos(): Map<ClassName, TypeInfoType> =
    asSequence()
        .map { it to it.getAnnotationsByType(TelegramCodegen.Type::class).first() }
        .associate { (classDeclaration, annotation) ->
            val className = classDeclaration.toClassName()
            className to TypeInfoType(className, classDeclaration, annotation)
        }

private fun Resolver.classDeclarationsAnnotatedWith(
    annotationName: String
): List<KSClassDeclaration> =
    getSymbolsWithAnnotation(annotationName)
        .filterIsInstance(KSClassDeclaration::class.java)
        .toList()

private val KSClassDeclaration.isConcreteCallable: Boolean
    get() = when (classKind) {
        ClassKind.OBJECT -> true
        ClassKind.CLASS ->
            Modifier.ABSTRACT !in modifiers &&
                    Modifier.SEALED !in modifiers
        else -> false
    }

private fun resolveCallableType(classDeclaration: KSClassDeclaration): CallableType {
    val callableSupertype = classDeclaration.getAllSuperTypes()
        .filter { it.declaration.qualifiedName?.asString() in CALLABLE_SUPERTYPES }
        .singleOrNull()
        ?: throw IllegalArgumentException(
            "$classDeclaration should have exactly one Telegram callable supertype"
        )
    val returnTypeArgument = callableSupertype.arguments.singleOrNull()
        ?: throw IllegalArgumentException("$classDeclaration is not a proper tg method")
    val resolvedReturnType = returnTypeArgument.type?.resolve()
        ?: throw IllegalArgumentException(
            "$classDeclaration has no concrete Telegram callable return type"
        )
    if (resolvedReturnType.containsTypeParameter()) {
        throw IllegalStateException(
            "Generic type substitution through intermediate Telegram callable classes " +
                    "is not supported: $classDeclaration"
        )
    }
    val transport = when (callableSupertype.declaration.qualifiedName?.asString()) {
        JSON_CALLABLE -> CallableTransport.JSON
        MULTIPART_CALLABLE -> CallableTransport.MULTIPART
        else -> error("Unsupported Telegram callable type $callableSupertype")
    }
    return CallableType(returnTypeArgument.toTypeName(), transport)
}

private fun KSType.containsTypeParameter(): Boolean =
    declaration is KSTypeParameter ||
            arguments.any { it.type?.resolve()?.containsTypeParameter() == true }

private const val METHOD_ANNOTATION =
    "ski.gagar.vertigram.annotations.TelegramCodegen.Method"
private const val TYPE_ANNOTATION =
    "ski.gagar.vertigram.annotations.TelegramCodegen.Type"
private const val JSON_CALLABLE =
    "ski.gagar.vertigram.telegram.types.methods.JsonTelegramCallable"
private const val MULTIPART_CALLABLE =
    "ski.gagar.vertigram.telegram.types.methods.MultipartTelegramCallable"
private val CALLABLE_SUPERTYPES = setOf(JSON_CALLABLE, MULTIPART_CALLABLE)
