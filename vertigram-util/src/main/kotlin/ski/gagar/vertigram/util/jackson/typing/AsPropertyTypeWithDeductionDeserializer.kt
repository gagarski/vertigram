package ski.gagar.vertigram.util.jackson.typing

import com.fasterxml.jackson.annotation.JsonTypeInfo.As
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.util.JsonParserSequence
import com.fasterxml.jackson.databind.DeserializationConfig
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver
import com.fasterxml.jackson.databind.jsontype.impl.AsDeductionTypeDeserializer
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver
import com.fasterxml.jackson.databind.util.TokenBuffer

/**
 * [AsPropertyTypeDeserializer] which falls back to type deduction for remaining types
 * if there is a multiple candidates with same type name.
 */
class AsPropertyTypeWithDeductionDeserializer(
    bt: JavaType,
    subtypes: Collection<NamedType>,
    config: DeserializationConfig,
    idRes: TypeIdResolver,
    typePropertyName: String,
    typeIdVisible: Boolean,
    defaultImpl: JavaType?,
    inclusion: As,
    strictTypeIdHandling: Boolean,
    subTypeValidator: PolymorphicTypeValidator
) : AsPropertyTypeDeserializer(
    bt,
    idRes,
    typePropertyName,
    typeIdVisible,
    defaultImpl,
    inclusion,
    strictTypeIdHandling
) {
    private val deducers = getDeducers(
        bt = bt,
        subtypes = subtypes,
        defaultImpl = defaultImpl,
        config = config,
        subTypeValidator = subTypeValidator
    )

    override fun _deserializeTypedForId(
        p: JsonParser,
        ctxt: DeserializationContext,
        tb: TokenBuffer?,
        typeId: String?
    ): Any {
        val tb = if (_typeIdVisible) {
            (tb ?: ctxt.bufferForInputBuffering(p))?.apply {
                writeFieldName(p.currentName())
                writeString(typeId)
            }
        } else {
            tb
        }
        val p = if (tb != null) {
            p.clearCurrentToken()
            JsonParserSequence.createFlattened(false, tb.asParser(p), p)
        } else {
            p
        }
        if (p.currentToken() != JsonToken.END_OBJECT) {
            // Must point to the next value; tb had no current, p pointed to VALUE_STRING:
            p.nextToken() // to skip past String value
        }

        val deducer = deducers[typeId] ?: return _deserializeTypedUsingDefaultImpl(p, ctxt, tb, _msgForMissingId)

        return deducer.deserializeTypedFromAny(p, ctxt)
    }


    companion object {
        private fun getDeducers(
            bt: JavaType,
            subtypes: Collection<NamedType>,
            defaultImpl: JavaType?,
            config: DeserializationConfig,
            subTypeValidator: PolymorphicTypeValidator
        ): Map<String, AsDeductionTypeDeserializer> {
            val idRes = ClassNameIdResolver.construct(bt, config, subTypeValidator)
            return subtypes
                .groupBy {
                    it.getId(config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES))
                }.mapValues { (k, v) ->
                    AsDeductionTypeDeserializer(bt, idRes, defaultImpl, config, v)
                }
        }

        private fun NamedType.getId(caseInsensitive: Boolean): String {
            // no name? Need to figure out default; for now, let's just
            // use non-qualified class name
            val cls: Class<*> = getType()
            val id = if (hasName()) getName() else defaultTypeId(cls)

            return if (caseInsensitive) id.lowercase() else id
        }

        private fun defaultTypeId(cls: Class<*>): String {
            val n = cls.name
            val ix = n.lastIndexOf('.')
            return if ((ix < 0)) n else n.substring(ix + 1)
        }
    }
}