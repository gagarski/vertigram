package ski.gagar.vertigram.util.json

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import java.time.Duration
import java.time.Instant

internal object TelegramModule : Module() {
    override fun getModuleName(): String = "TelegramModule"

    override fun version(): Version = Version.unknownVersion()

    override fun setupModule(context: SetupContext) {
        context.addSerializers(SimpleSerializers().apply {
            addSerializer(UnixTimestampSerializer())
            addSerializer(AttachmentSerializer())
            addSerializer(UrlAttachmentSerializer())
            addSerializer(DurationInSecondsSerializer())
        })
        context.addDeserializers(SimpleDeserializers().apply {
            addDeserializer(
                Instant::class.java,
                UnixTimestampDeserializer()
            )
            // Useless, fails to resolve type
//            addDeserializer(
//                Attachment::class.java,
//                AttachmentDeserializer()
//            )
            addDeserializer(
                Duration::class.java,
                DurationInSecondsDeserializer()
            )
        })
    }

}
