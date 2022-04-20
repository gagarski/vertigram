package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.databind.introspect.AnnotatedMember
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector

internal class TelegramAnnotationIntrospector : NopAnnotationIntrospector() {
    override fun hasIgnoreMarker(m: AnnotatedMember): Boolean {
        if (m.hasAnnotation(TgIgnore::class.java))
            return true

        return super.hasIgnoreMarker(m)
    }
}
