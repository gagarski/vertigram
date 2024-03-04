package ski.gagar.vertigram.util.jackson.typing

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder

/**
 * [StdTypeResolverBuilder] which overrides [buildTypeDeserializer] returning [AsPropertyTypeWithDeductionDeserializer].
 *
 * Currently, supports only [JsonTypeInfo.Id.NAME] with combination of
 * [JsonTypeInfo.As.PROPERTY] or [JsonTypeInfo.As.EXISTING_PROPERTY]
 */
class TypeResolverWithDeductionBuilder : StdTypeResolverBuilder {
    constructor() : super()

    private constructor(base: TypeResolverWithDeductionBuilder, defaultImpl: Class<*>) : super(base, defaultImpl)

    override fun withDefaultImpl(defaultImpl: Class<*>): TypeResolverWithDeductionBuilder {
        if (_defaultImpl == defaultImpl) {
            return this
        }
        return TypeResolverWithDeductionBuilder(this, defaultImpl)

    }

    override fun withSettings(settings: JsonTypeInfo.Value): StdTypeResolverBuilder {
        checkId(settings.idType)
        checkAs(settings.inclusionType)
        return super.withSettings(settings)
    }

    override fun buildTypeDeserializer(
        config: DeserializationConfig,
        baseType: JavaType,
        subtypes: MutableCollection<NamedType>
    ): TypeDeserializer? {
        if (baseType.isPrimitive) {
            if (!allowPrimitiveTypes(config, baseType)) {
                return null
            }
        }

        val bean = config.introspectClassAnnotations(baseType.rawClass)
        val ac = bean.classInfo


        @Suppress("NAME_SHADOWING")
        val subtypes = StdSubtypeResolverWithDuplicates(config.subtypeResolver as StdSubtypeResolver)
            .collectAndResolveSubtypesByTypeId(config, ac)

        val subTypeValidator = verifyBaseTypeValidity(config, baseType)
        val idRes = idResolver(config, baseType, subTypeValidator, subtypes, false, true)
        val defaultImpl = defineDefaultImpl(config, baseType)

        return AsPropertyTypeWithDeductionDeserializer(
            bt = baseType,
            subtypes = subtypes,
            config = config,
            idRes = idRes,
            defaultImpl = defaultImpl,
            typePropertyName = _typeProperty,
            typeIdVisible = _typeIdVisible,
            inclusion = _includeAs,
            strictTypeIdHandling = _strictTypeIdHandling(config, baseType),
            subTypeValidator = subTypeValidator
        )
    }


    companion object {
        fun checkId(id: JsonTypeInfo.Id) = id.also {
            require(id == JsonTypeInfo.Id.NAME) {
                "$id type info id is unsupported"
            }
        }

        fun checkAs(`as`: JsonTypeInfo.As) = `as`.also {
            require(`as` == JsonTypeInfo.As.PROPERTY || `as` == JsonTypeInfo.As.EXISTING_PROPERTY)
        }
    }
}