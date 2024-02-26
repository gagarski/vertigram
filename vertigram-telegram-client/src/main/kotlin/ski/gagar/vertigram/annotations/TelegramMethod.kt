package ski.gagar.vertigram.annotations

import java.lang.annotation.Inherited

/**
 * Annotation for Telegram method, used to give hints for deserialization and Telegram verticle generation.

 * This annotation is [Inherited]. To override annotation parameters in a subclass,
 * just add the annotation directly to it.
 */
@Inherited
annotation class TelegramMethod(
    /**
     * Telegram method to call. By default, it's class name with lowercase first letter.
     */
    val methodName: String = "",
    /**
     * Should consumer in `TelegramVerticle` be generated
     */
    val generateVerticleConsumer: Boolean = true,
    /**
     * A part of `TelegramVerticle` consumer address for this method. By default, it's class name with lowercase first letter.
     */
    val verticleConsumerName: String = ""
)
