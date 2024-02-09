package ski.gagar.vertigram.annotations

import java.lang.annotation.Inherited

@Inherited
@Retention(AnnotationRetention.SOURCE)
annotation class TelegramCodegen(
    val generateMethod: Boolean = true,
    val methodName: String = "",
    val generatePseudoConstructor: Boolean = false,
    val pseudoConstructorName: String = ""
)
