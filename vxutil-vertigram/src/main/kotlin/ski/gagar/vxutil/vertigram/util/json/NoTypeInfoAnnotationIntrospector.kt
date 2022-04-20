package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import ski.gagar.vxutil.uncheckedCast

internal class NoTypeInfoAnnotationIntrospector : JacksonAnnotationIntrospector() {
    override fun _hasAnnotation(annotated: Annotated, annoClass: Class<out Annotation>?): Boolean {
        if (annoClass != JsonTypeInfo::class.java)
            return super._hasAnnotation(annotated, annoClass)

        return !_hasAnnotation(annotated, TgIgnoreTypeInfo::class.java)
    }

    override fun <A : Annotation> _findAnnotation(
        annotated: Annotated,
        annoClass: Class<A>
    ): A? {
        if (annoClass != JsonTypeInfo::class.java)
            return super._findAnnotation(annotated, annoClass)

        if (_findAnnotation(annotated, TgIgnoreTypeInfo::class.java) != null) {
            return null
        }

        return super._findAnnotation(annotated, annoClass.uncheckedCast<Class<A>>())
    }

}
