package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import ski.gagar.vertigram.annotations.TelegramCodegen

internal data class TypeInfoMethod(
    val className: ClassName,
    val classDecl: KSClassDeclaration,
    val annotation: TelegramCodegen.Method,
    val callableType: CallableType?
)

internal data class TypeInfoType(
    val className: ClassName,
    val classDecl: KSClassDeclaration,
    val annotation: TelegramCodegen.Type
)

internal data class CallableType(
    val returnType: TypeName,
    val transport: CallableTransport
)

internal enum class CallableTransport {
    JSON,
    MULTIPART
}

internal data class TelegramMethodNames(
    val methodName: String,
    val telegramName: String
)

internal fun telegramMethodNames(
    classDeclaration: KSClassDeclaration,
    annotation: TelegramCodegen.Method
): TelegramMethodNames {
    val methodName = annotation.name.ifEmpty {
        classDeclaration.simpleName.getShortName().replaceFirstChar(Char::lowercaseChar)
    }
    val telegramName = annotation.telegramName.ifEmpty { methodName }
    return TelegramMethodNames(methodName, telegramName)
}
