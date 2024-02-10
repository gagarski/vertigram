package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import ski.gagar.vertigram.uncheckedCast

internal class TelegramAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun _hasAnnotation(annotated: Annotated, annoClass: Class<out Annotation>?): Boolean {
        if (annoClass != JsonTypeInfo::class.java)
            return super._hasAnnotation(annotated, annoClass)

        return !_hasAnnotation(annotated, TelegramIgnoreTypeInfo::class.java)
    }

    override fun <A : Annotation> _findAnnotation(
        annotated: Annotated,
        annoClass: Class<A>
    ): A? {
        if (annoClass != JsonTypeInfo::class.java)
            return super._findAnnotation(annotated, annoClass)

        if (_findAnnotation(annotated, TelegramIgnoreTypeInfo::class.java) != null) {
            return null
        }

        return super._findAnnotation(annotated, annoClass.uncheckedCast<Class<A>>())
    }

    override fun findNameForSerialization(a: Annotated): PropertyName? {
        val name = super.findNameForSerialization(a)
        return when {
            null == name -> {
                null
            }
            name.simpleName.endsWith(UNWRAPPED) -> {
                PropertyName.construct(name.simpleName.removeSuffix(UNWRAPPED), name.namespace)
            }
            else -> {
                name
            }
        }
    }

    companion object {
        const val UNWRAPPED = "_unwrapped"
    }

}
