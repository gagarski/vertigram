package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import java.time.Duration
import java.time.Instant

/**
 * Jackson modules that defines serializers and serializers for some standard types in Telegram-specific format
 */
internal object TelegramModule : Module() {
    override fun getModuleName(): String = "TelegramModule"

    override fun version(): Version = Version.unknownVersion()

    override fun setupModule(context: SetupContext) {
        context.addSerializers(SimpleSerializers().apply {
            addSerializer(AttachmentSerializer())
            addSerializer(UrlAttachmentSerializer())
            addSerializer(DurationInSecondsSerializer())
            addSerializer(UnixTimestampSerializer())
        })
        context.addDeserializers(SimpleDeserializers().apply {
            addDeserializer(
                Instant::class.java,
                UnixTimestampDeserializer()
            )
            addDeserializer(
                Duration::class.java,
                DurationInSecondsDeserializer()
            )
        })
    }

}
