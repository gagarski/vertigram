package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

class VertigramClientGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private val typeHintsGenerator = VertigramTypeHintsGenerator(codeGenerator, logger)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val methodDeclarations = resolver.telegramMethodDeclarations()
        val typeDeclarations = resolver.telegramTypeDeclarations()
        validateDeclarations(methodDeclarations.asSequence() + typeDeclarations.asSequence())

        val methods = methodDeclarations.toMethodInfos()
        val types = typeDeclarations.toTypeInfos()
        val fileSpecBuilders = mutableMapOf<FileSpecBuilderKey, FileSpec.Builder>()

        methods.values.forEach { it.addClientMethodTo(fileSpecBuilders) }
        types.values.forEach { it.addCreatorFunctionsTo(fileSpecBuilders) }
        typeHintsGenerator.generate(methods.values)

        fileSpecBuilders.values.forEach {
            it.build().writeTo(codeGenerator, Dependencies(aggregating = true))
        }

        return emptyList()
    }

    private fun validateDeclarations(declarations: Sequence<KSClassDeclaration>) {
        val invalidDeclarations = declarations
            .filterNot(KSClassDeclaration::validate)
            .toList()
        if (invalidDeclarations.isEmpty()) return

        invalidDeclarations.forEach {
            logger.error(
                "Telegram code generation does not support declarations with unresolved types",
                it
            )
        }
        throw IllegalStateException(
            "Telegram code generation failed: " +
                    "${invalidDeclarations.size} declaration(s) have unresolved types"
        )
    }
}
