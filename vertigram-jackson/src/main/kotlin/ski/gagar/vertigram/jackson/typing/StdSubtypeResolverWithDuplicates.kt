package ski.gagar.vertigram.jackson.typing

import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver
import java.lang.reflect.Modifier

/**
 * Hacked version of [StdSubtypeResolver] which overrides [collectAndResolveSubtypesByTypeId] so
 * that it returns the result with duplicates
 */
class StdSubtypeResolverWithDuplicates : StdSubtypeResolver {
    constructor() : super()
    constructor(std: StdSubtypeResolver) : super(std)


    /**
     * Monkey-patched version of [collectAndResolveSubtypesByTypeId] from superclass
     * that returns a [Collection] of [NamedType] with duplicates by name if there are any
     */
    override fun collectAndResolveSubtypesByTypeId(
        config: MapperConfig<*>,
        baseType: AnnotatedClass
    ): Collection<NamedType> {
        val rawBase = baseType.rawType
        val typesHandled: MutableSet<Class<*>> = mutableSetOf()
        val collected: MutableSet<NamedType> = mutableSetOf()

        val rootType = NamedType(rawBase, null)
        _collectAndResolveByTypeId(baseType, rootType, config, typesHandled, collected)

        for (subtype in _registeredSubtypes.orEmpty()) {
            if (rawBase.isAssignableFrom(subtype.type)) {
                val curr = AnnotatedClassResolver.resolveWithoutSuperTypes(
                    config,
                    subtype.type
                )
                _collectAndResolveByTypeId(curr, subtype, config, typesHandled, collected)
            }
        }

        return _combineNamedAndUnnamed(rawBase, typesHandled, collected)
    }

    private fun _collectAndResolveByTypeId(
        annotatedType: AnnotatedClass?, namedType: NamedType,
        config: MapperConfig<*>,
        typesHandled: MutableSet<Class<*>>, collected: MutableSet<NamedType>
    ) {
        val ai = config.annotationIntrospector
        @Suppress("NAME_SHADOWING")
        val namedType =
            if (namedType.hasName())
                namedType
            else {
                val name = ai.findTypeName(annotatedType)
                if (name != null)
                    NamedType(namedType.type, name)
                else
                    namedType
            }

        if (namedType.hasName()) {
            collected.add(namedType)
        }

        if (typesHandled.add(namedType.type)) {
            for (subtype in ai.findSubtypes(annotatedType).orEmpty()) {
                val subtypeClass = AnnotatedClassResolver.resolveWithoutSuperTypes(
                    config,
                    subtype.type
                )
                _collectAndResolveByTypeId(subtypeClass, subtype, config, typesHandled, collected)
            }
        }
    }

    private fun _combineNamedAndUnnamed(
        rawBase: Class<*>,
        typesHandled: MutableSet<Class<*>>, collected: MutableSet<NamedType>
    ): Collection<NamedType> {
        val result = ArrayList(collected)

        for (t in collected) {
            typesHandled.remove(t.type)
        }
        for (cls in typesHandled) {
            if ((cls == rawBase) && Modifier.isAbstract(cls.modifiers)) {
                continue
            }
            result.add(NamedType(cls))
        }
        return result
    }
}