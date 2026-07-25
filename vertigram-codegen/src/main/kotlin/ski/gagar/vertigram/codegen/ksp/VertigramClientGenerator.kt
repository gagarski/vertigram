package ski.gagar.vertigram.codegen.ksp

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAllSuperTypes
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MAP
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import ski.gagar.vertigram.annotations.TelegramCodegen

class VertigramClientGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val methodDeclarations = resolver.getClassDeclarations(METHOD_ANNOTATION)
        val typeDeclarations = resolver.getClassDeclarations(TYPE_ANNOTATION)
        validateDeclarations(methodDeclarations.asSequence() + typeDeclarations.asSequence())

        val methods = methodDeclarations.toMethodInfos()
        val types = typeDeclarations.toTypeInfos()
        val builders = mutableMapOf<FileSpecBuilderKey, FileSpec.Builder>()
        for (clazz in methods.values) {
            clazz.processMethod(builders)
        }
        generateVertigramTypeHints(methods.values)

        for (clazz in types.values) {
            clazz.processType(builders)
        }

        for (bld in builders.values) {
            bld.build().writeTo(codeGenerator, Dependencies(true))
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
            "Telegram code generation failed: ${invalidDeclarations.size} declaration(s) have unresolved types"
        )
    }

    private fun generateVertigramTypeHints(methods: Collection<TypeInfoMethod>) {
        if (methods.isEmpty()) return

        val callables = methods
            .asSequence()
            .filter { it.callableType != null }
            .sortedBy { it.className.canonicalName }
            .toList()
        validateUniqueTgvAddresses(callables)

        val classStar = ClassName("java.lang", "Class").parameterizedBy(STAR)
        val descriptorClass = ClassName(TYPE_HINTS_PACKAGE, CALLABLE_DESCRIPTOR)
        val transportClass = ClassName(TYPE_HINTS_PACKAGE, CALLABLE_TRANSPORT)

        val typeHints = TypeSpec.objectBuilder(TYPE_HINTS_OBJECT)
            .addAnnotation(
                AnnotationSpec.builder(Suppress::class)
                    .addMember("%S", "DEPRECATION")
                    .build()
            )
            .addKdoc("Type hints used by Vertigram to dispatch and deserialize Telegram methods.\n")
            .addProperty(
                PropertySpec.builder(
                    "descriptorByCallable",
                    MAP.parameterizedBy(classStar, descriptorClass)
                )
                    .addKdoc("Concrete Telegram callable classes associated with their descriptors.\n")
                    .initializer(
                        CodeBlock.builder()
                            .add("mapOf<%T, %T>(\n", classStar, descriptorClass)
                            .indent()
                            .apply {
                                callables.forEach {
                                    add(
                                        "%T::class.java to %L,\n",
                                        it.className,
                                        it.descriptorCodeBlock(descriptorClass, transportClass)
                                    )
                                }
                            }
                            .unindent()
                            .add(")")
                            .build()
                    )
                    .build()
            )
            .addProperty(
                PropertySpec.builder(
                    "descriptorByTgvAddress",
                    MAP.parameterizedBy(STRING, descriptorClass)
                )
                    .addKdoc("Descriptors indexed by callable-specific address segment, derived at runtime.\n")
                    .initializer("descriptorByCallable.values.associateBy(%T::tgvAddress)", descriptorClass)
                    .build()
            )

        FileSpec.builder(TYPE_HINTS_PACKAGE, GENERATED_TYPE_HINTS)
            .addType(typeHints.build())
            .build()
            .writeTo(
                codeGenerator,
                Dependencies(
                    aggregating = true,
                    *methods.mapNotNull { it.classDecl.containingFile }.distinct().toTypedArray()
                )
            )
    }

    private fun validateUniqueTgvAddresses(callables: Collection<TypeInfoMethod>) {
        val collisions = callables
            .groupBy { it.tgvAddress }
            .filterValues { it.size > 1 }
        if (collisions.isEmpty()) return

        collisions.forEach { (address, methods) ->
            val classes = methods.joinToString { it.className.canonicalName }
            methods.forEach {
                logger.error("Duplicate Vertigram address '$address' used by $classes", it.classDecl)
            }
        }
        throw IllegalStateException(
            "Telegram code generation failed: ${collisions.size} duplicate Vertigram address(es)"
        )
    }

    private fun TypeInfoMethod.descriptorCodeBlock(
        descriptorClass: ClassName,
        transportClass: ClassName
    ): CodeBlock {
        val callableType = requireNotNull(callableType)
        return CodeBlock.builder()
            .add("%T(\n", descriptorClass)
            .indent()
            .add("callableClass = %T::class.java,\n", className)
            .add("telegramMethodName = %S,\n", getNames(classDecl, annotation).telegramName)
            .add("tgvAddress = %S,\n", tgvAddress)
            .add("generateVerticleConsumer = %L,\n", annotation.generateVerticleConsumer)
            .add(
                "requestType = TELEGRAM_TYPE_FACTORY.constructType(%T::class.java),\n",
                className
            )
            .add("responseType = %L,\n", callableType.returnType.javaTypeCodeBlock())
            .add("transport = %T.%L,\n", transportClass, callableType.transport.name)
            .unindent()
            .add(")")
            .build()
    }

    private fun TypeName.javaTypeCodeBlock(): CodeBlock =
        if (this is ClassName) {
            CodeBlock.of(
                "TELEGRAM_TYPE_FACTORY.constructType(%T::class.%M)",
                this,
                JAVA_OBJECT_TYPE
            )
        } else {
            val typeReference = ClassName(
                "com.fasterxml.jackson.core.type",
                "TypeReference"
            ).parameterizedBy(this)
            CodeBlock.of(
                "TELEGRAM_TYPE_FACTORY.constructType(object : %T() {}.type)",
                typeReference
            )
        }

    private val TypeInfoMethod.tgvAddress: String
        get() = annotation.verticleConsumerName.ifEmpty {
            className.simpleNames
                .joinToString(".") { it.replaceFirstChar(Char::lowercaseChar) }
        }

    private fun TypeInfoMethod.processMethod(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>
    ) {
        val (className, classDecl, annotation) = this
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
                methodsFile.addFunction(
                    kotlinMethodForObject(classDecl, className, annotation, resolvedCallableType.returnType)
                )
            }
            ClassKind.CLASS -> {
                val method = kotlinMethodForClass(classDecl, className, annotation, resolvedCallableType.returnType)
                methodsFile.addFunction(method)
            }
            else -> error("Concrete callable $className has an unsupported kind ${classDecl.classKind}")

        }
    }

    private fun TypeInfoType.processType(
        fileSpecBuilders: MutableMap<FileSpecBuilderKey, FileSpec.Builder>
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
        classDecl.simpleName.getShortName().replaceFirstChar(Char::lowercaseChar)

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
                    } else if (param.type.resolve().rawClassName == DOUBLE) {
                        defaultValue("0.0")
                    } else if (param.type.resolve().rawClassName == ClassName("ski.gagar.vertigram.util", "NoPosArgs")) {
                        throw IllegalArgumentException("Please remove NoPosArgs from constructor for $className")
                    }
                }
            }.build()

    private fun FunSpec.Builder.addParametersFromPrimaryConstructor(
        classDecl: KSClassDeclaration,
        className: ClassName,
        actuallyWrapped: MutableMap<String, WrapConfig>,
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

        this.addParameter(
            ParameterSpec.builder(NO_POS_ARGS, NO_POS_ARGS_TYPE)
                .defaultValue("ski.gagar.vertigram.util.NoPosArgs.INSTANCE")
                .build()
        )

        val paramsSet = constructor.parameters.asSequence().map { it.name!!.getShortName() }.toSet()
        val triggers = mutableMapOf<String, WrapConfig>()
        for (wrapConfig in WRAP_CONFIGS) {
            if (paramsSet.containsAll(wrapConfig.wrapperParamMapping.keys)) {
                for (param in wrapConfig.wrapperParamMapping.keys) {
                    triggers[param] = wrapConfig
                }
            }
        }

        val alreadyWrapped = mutableSetOf<String>()

        for (param in constructor.parameters) {
            val wrapConfig = if (wrapRichText) triggers[param.name!!.getShortName()] else null

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

                alreadyWrapped.addAll(wrapConfig.wrapperParamMapping.keys)
                actuallyWrapped[wrapConfig.wrapperParam] = wrapConfig
            } else {
                this.addParameter(
                    constructorParamToMethodParam(param, defaults, className)
                )
            }
        }
    }

    private fun FunSpec.Builder.callPrimaryConstructor(
        className: ClassName,
        actuallyWrapped: Map<String, WrapConfig>
    ): FunctionCall {
        val format = sequence {
            for (param in parameters) {
                if (param.name == NO_POS_ARGS) continue
                val wrapperConfig =
                    if (param.name in actuallyWrapped)
                        actuallyWrapped[param.name] else null

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
                        actuallyWrapped[param.name] else null

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
        returnType: TypeName
    ): FunSpec {
        val names = getNames(classDecl, anno)

        val actuallyWrapped = mutableMapOf<String, WrapConfig>()
        return FunSpec.builder(names.methodName)
            .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"DEPRECATION\"").build())
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.telegram.client", "Telegram"))
            .returns(returnType)
            .apply {
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno.wrapRichText)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return call(${call.formatString})", *call.parameters.toTypedArray())
            }
            .addGeneratedKdoc(classDecl, className, actuallyWrapped, names.telegramName)
            .build()
    }

    private fun kotlinMethodForObject(
        classDecl: KSClassDeclaration,
        className: ClassName,
        anno: TelegramCodegen.Method,
        returnType: TypeName
    ): FunSpec {
        val names = getNames(classDecl, anno)

        return FunSpec.builder(names.methodName)
            .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("\"DEPRECATION\"").build())
            .addModifiers(KModifier.SUSPEND)
            .receiver(ClassName("ski.gagar.vertigram.telegram.client", "Telegram"))
            .returns(returnType)
            .addGeneratedKdoc(classDecl, className, telegramName = names.telegramName)
            .addStatement("return call(%T)", className)
            .build()
    }

    private val KSType.rawClassName: ClassName
        get() = (declaration as KSClassDeclaration).toClassName()

    private fun resolveCallableType(clazz: KSClassDeclaration): CallableType {
        val callableSupertype = clazz.getAllSuperTypes()
            .filter { it.declaration.qualifiedName?.asString() in SUPERTYPES }
            .singleOrNull()
            ?: throw IllegalArgumentException("$clazz should have exactly one Telegram callable supertype")
        val returnTypeArgument = callableSupertype.arguments.singleOrNull()
            ?: throw IllegalArgumentException("$clazz is not a proper tg method")
        val resolvedReturnType = returnTypeArgument.type?.resolve()
            ?: throw IllegalArgumentException("$clazz has no concrete Telegram callable return type")
        if (resolvedReturnType.containsTypeParameter()) {
            throw IllegalStateException(
                "Generic type substitution through intermediate Telegram callable classes is not supported: $clazz"
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

    private fun FunSpec.Builder.addGeneratedKdoc(
        classDecl: KSClassDeclaration,
        className: ClassName,
        actuallyWrapped: Map<String, WrapConfig> = emptyMap(),
        telegramName: String? = null
    ) = apply {
        val sourceKdoc = classDecl.mergedKdoc()
        if (sourceKdoc.isNotBlank()) {
            val generatedKdoc = sourceKdoc.rewriteForGeneratedFunction(
                actuallyWrapped,
                classDecl.constructorParameterKdocs(),
                parameters.map(ParameterSpec::name)
            )
            if (generatedKdoc.description.isNotBlank()) {
                addKdoc("%L\n\n", generatedKdoc.description)
            }
            addKdoc("Generated from [%T].", className)
            if (generatedKdoc.parameterTags.isNotBlank()) {
                addKdoc("\n\n%L\n", generatedKdoc.parameterTags)
            }
        } else if (telegramName != null) {
            addKdoc(
                "Telegram [%L](https://core.telegram.org/bots/api#%L) method.\n\n" +
                        "For up-to-date documentation, please consult the official Telegram docs.\n\n" +
                        "This function is auto-generated from [%T].\n",
                telegramName,
                telegramName.lowercase(),
                className
            )
            return@apply
        } else {
            addKdoc("Auto-generated function, please see [%T] docs.", className)
            return@apply
        }
    }

    private fun KSClassDeclaration.mergedKdoc(): String =
        generateSequence(this as KSDeclaration?) { it.parentDeclaration }
            .filterIsInstance<KSClassDeclaration>()
            .toList()
            .asReversed()
            .mapNotNull(KSClassDeclaration::docString)
            .map { it.normalizeKdoc() }
            .filter(String::isNotBlank)
            .distinct()
            .joinToString("\n\n")

    private fun String.normalizeKdoc(): String =
        lineSequence()
            .map { line ->
                line.trim()
                    .removePrefix("/**")
                    .removeSuffix("*/")
                    .removePrefix("*")
                    .trimStart()
            }
            .dropWhile(String::isBlank)
            .toList()
            .dropLastWhile(String::isBlank)
            .joinToString("\n")

    private fun String.rewriteForGeneratedFunction(
        actuallyWrapped: Map<String, WrapConfig>,
        constructorParameterKdocs: Map<String, String>,
        generatedParameterNames: List<String>
    ): GeneratedFunctionKdoc {
        val lines = lines()
        val retained = mutableListOf<String>()
        val taggedParameterKdocs = mutableMapOf<String, StringBuilder>()
        var currentParameter: String? = null

        for (line in lines) {
            val match = PARAM_TAG.matchEntire(line.trim())
            if (match != null) {
                currentParameter = match.groupValues[2]
                taggedParameterKdocs.getOrPut(currentParameter, ::StringBuilder)
                    .append(match.groupValues[3].trim())
            } else if (currentParameter != null && !line.trimStart().startsWith("@")) {
                taggedParameterKdocs.getValue(currentParameter)
                    .append(' ')
                    .append(line.trim())
            } else {
                currentParameter = null
                retained += line
            }
        }

        val wrapperDocs = actuallyWrapped.mapValues { (_, config) ->
            val sourceDescriptions = config.wrapperParamMapping.keys
                .mapNotNull { parameterName ->
                    constructorParameterKdocs[parameterName]
                        ?: taggedParameterKdocs[parameterName]
                            ?.toString()
                            ?.replace(WHITESPACE, " ")
                            ?.trim()
                            ?.takeIf(String::isNotEmpty)
                }
                .distinct()
            buildString {
                append("Rich-text value supplying ")
                append(config.wrapperParamMapping.keys.joinToString { "`$it`" })
                append(" to the Telegram request.")
                if (sourceDescriptions.isNotEmpty()) {
                    append(' ')
                    append(sourceDescriptions.joinToString(" "))
                }
            }
        }
        val generatedParameterDocs = generatedParameterNames.mapNotNull { parameterName ->
            if (parameterName == NO_POS_ARGS) return@mapNotNull null
            val documentation = wrapperDocs[parameterName]
                ?: constructorParameterKdocs[parameterName]
                ?: taggedParameterKdocs[parameterName]
                    ?.toString()
                    ?.replace(WHITESPACE, " ")
                    ?.trim()
                    ?.takeIf(String::isNotEmpty)
                ?: return@mapNotNull null
            wrapParameterKdoc(parameterName, documentation)
        }
        return GeneratedFunctionKdoc(
            description = retained.dropLastWhile(String::isBlank).joinToString("\n"),
            parameterTags = generatedParameterDocs.joinToString("\n")
        )
    }

    private data class GeneratedFunctionKdoc(
        val description: String,
        val parameterTags: String
    )

    private fun wrapParameterKdoc(parameterName: String, documentation: String): String {
        val firstLinePrefix = "@param $parameterName "
        val words = documentation.split(WHITESPACE)
        val lines = mutableListOf<String>()
        var prefix = firstLinePrefix
        var currentLine = StringBuilder(prefix)

        for (word in words) {
            val separatorLength = if (currentLine.length == prefix.length) 0 else 1
            if (
                currentLine.length > prefix.length &&
                currentLine.length + separatorLength + word.length > KDOC_CONTENT_WIDTH
            ) {
                lines += currentLine.toString()
                prefix = KDOC_TAG_CONTINUATION
                currentLine = StringBuilder(prefix)
            }
            if (currentLine.length > prefix.length) currentLine.append(' ')
            currentLine.append(word)
        }
        lines += currentLine.toString()
        return lines.joinToString("\n")
    }

    private fun KSClassDeclaration.constructorParameterKdocs(): Map<String, String> =
        declarations
            .filterIsInstance<KSPropertyDeclaration>()
            .filter { property ->
                primaryConstructor?.parameters?.any {
                    it.name?.getShortName() == property.simpleName.getShortName()
                } == true
            }
            .mapNotNull { property ->
                val documentation = property.docString
                    ?.normalizeKdoc()
                    ?.replace(WHITESPACE, " ")
                    ?.trim()
                    ?.takeIf(String::isNotBlank)
                    ?: return@mapNotNull null
                property.simpleName.getShortName() to documentation
            }
            .toMap()

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

        val actuallyWrapped = mutableMapOf<String, WrapConfig>()
        return FunSpec.builder(name)
            .returns(className)
            .receiver(ClassName(className.packageName, receiver))
            .apply {

                if (isOperator) {
                    addModifiers(KModifier.OPERATOR)
                }
                addParametersFromPrimaryConstructor(classDecl, className, actuallyWrapped, anno.wrapRichText)
                val call = callPrimaryConstructor(className, actuallyWrapped)
                addStatement("return ${call.formatString}", *call.parameters.toTypedArray())
            }
            .addGeneratedKdoc(classDecl, className, actuallyWrapped)
            .build()
    }

    private fun Resolver.getClassDeclarations(annotationName: String): List<KSClassDeclaration> =
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

    @OptIn(KspExperimental::class)
    private fun Iterable<KSClassDeclaration>.toMethodInfos(): Map<ClassName, TypeInfoMethod> =
        asSequence()
            .map { it to it.getAnnotationsByType(TelegramCodegen.Method::class).first() }
            .associate { (classDecl, annotation) ->
                val className = classDecl.toClassName()
                className to TypeInfoMethod(
                    className,
                    classDecl,
                    annotation,
                    classDecl.takeIf { it.isConcreteCallable }?.let(::resolveCallableType)
                )
            }

    @OptIn(KspExperimental::class)
    private fun Iterable<KSClassDeclaration>.toTypeInfos(): Map<ClassName, TypeInfoType> =
        asSequence()
            .map { it to it.getAnnotationsByType(TelegramCodegen.Type::class).first() }
            .associate { (classDecl, annotation) ->
                val className = classDecl.toClassName()
                className to TypeInfoType(className, classDecl, annotation)
            }

    private data class TypeInfoMethod(
        val className: ClassName,
        val classDecl: KSClassDeclaration,
        val annotation: TelegramCodegen.Method,
        val callableType: CallableType?
    )

    private data class TypeInfoType(
        val className: ClassName,
        val classDecl: KSClassDeclaration,
        val annotation: TelegramCodegen.Type
    )

    private data class CallableType(
        val returnType: TypeName,
        val transport: CallableTransport
    )

    private enum class CallableTransport {
        JSON,
        MULTIPART
    }

    private data class FileSpecBuilderKey(val packageName: String, val fileName: String)

    private data class FunctionCall(
        val formatString: String,
        val parameters: List<Any>
    )

    private data class WrapConfig(
        val wrapper: ClassName,
        val wrapperParam: String,
        val wrapperParamMapping: Map<String, String>
    )

    private data class Names(val methodName: String, val telegramName: String)

    companion object {
        private const val METHOD_ANNOTATION =
            "ski.gagar.vertigram.annotations.TelegramCodegen.Method"
        private const val TYPE_ANNOTATION =
            "ski.gagar.vertigram.annotations.TelegramCodegen.Type"
        private const val TG_METHODS = "TelegramMethods"
        private const val TG_CREATORS = "TelegramCreators"
        private const val TG_CONSTRUCTORS = "TelegramConstructors"
        private const val JSON_CALLABLE =
            "ski.gagar.vertigram.telegram.types.methods.JsonTelegramCallable"
        private const val MULTIPART_CALLABLE =
            "ski.gagar.vertigram.telegram.types.methods.MultipartTelegramCallable"
        private val SUPERTYPES = setOf(JSON_CALLABLE, MULTIPART_CALLABLE)
        private val WRAP_CONFIGS = listOf(
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richTitle",
                wrapperParamMapping = mapOf(
                    "title" to "text",
                    "parseMode" to "parseMode",
                    "titleEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richQuestion",
                wrapperParamMapping = mapOf(
                    "question" to "text",
                    "questionParseMode" to "parseMode",
                    "questionEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richDescription",
                wrapperParamMapping = mapOf(
                    "description" to "text",
                    "descriptionParseMode" to "parseMode",
                    "descriptionEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richCaption",
                wrapperParamMapping = mapOf(
                    "caption" to "text",
                    "parseMode" to "parseMode",
                    "captionEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richText",
                wrapperParamMapping = mapOf(
                    "text" to "text",
                    "parseMode" to "parseMode",
                    "entities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richText",
                wrapperParamMapping = mapOf(
                    "text" to "text",
                    "parseMode" to "parseMode",
                    "textEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richMessageText",
                wrapperParamMapping = mapOf(
                    "messageText" to "text",
                    "parseMode" to "parseMode",
                    "entities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richQuote",
                wrapperParamMapping = mapOf(
                    "quote" to "text",
                    "quoteParseMode" to "parseMode",
                    "quoteEntities" to "entities"
                )
            ),
            WrapConfig(
                wrapper = ClassName("ski.gagar.vertigram.telegram.types.richtext", "RichText"),
                wrapperParam = "richExplanation",
                wrapperParamMapping = mapOf(
                    "explanation" to "text",
                    "explanationParseMode" to "parseMode",
                    "explanationEntities" to "entities"
                )
            ),
        )

        private val NO_POS_ARGS_TYPE = ClassName("ski.gagar.vertigram.util", "NoPosArgs")
        private val JAVA_OBJECT_TYPE = MemberName("kotlin.jvm", "javaObjectType")
        private const val NO_POS_ARGS = "noPosArgs"
        private const val METHODS_PACKAGE = "ski.gagar.vertigram.telegram.methods"
        private const val TYPE_HINTS_PACKAGE = "ski.gagar.vertigram.util"
        private const val GENERATED_TYPE_HINTS = "GeneratedVertigramTypeHints"
        private const val TYPE_HINTS_OBJECT = "VertigramTypeHints"
        private const val CALLABLE_DESCRIPTOR = "TelegramCallableDescriptor"
        private const val CALLABLE_TRANSPORT = "TelegramCallableTransport"
        private const val CREATE = "create"
        private const val INVOKE = "invoke"
        private const val KDOC_CONTENT_WIDTH = 117
        private const val KDOC_TAG_CONTINUATION = "    "
        private val PARAM_TAG = Regex("""@(param|property)\s+(\w+)\s*(.*)""")
        private val WHITESPACE = Regex("""\s+""")
    }
}
