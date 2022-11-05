package ski.gagar.vxutil.vertigram.util.json

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleSerializers
import ski.gagar.vxutil.vertigram.types.ChatId
import ski.gagar.vxutil.vertigram.types.RgbColor
import ski.gagar.vxutil.vertigram.types.attachments.Attachment
import java.time.Duration
import java.time.Instant

internal object TelegramModule : Module() {
    override fun getModuleName(): String = "TelegramModule"

    override fun version(): Version = Version.unknownVersion()

    override fun setupModule(context: SetupContext) {
        context.appendAnnotationIntrospector(TelegramAnnotationIntrospector())

        context.addSerializers(SimpleSerializers().apply {
            addSerializer(UnixTimestampSerializer())
            addSerializer(ChatIdSerializer())
            addSerializer(AttachmentSerializer())
            addSerializer(UrlAttachmentSerializer())
            addSerializer(DurationInSecondsSerializer())
            addSerializer(RgbColorSerializer())
        })
        context.addDeserializers(SimpleDeserializers().apply {
            addDeserializer(
                Instant::class.java,
                UnixTimestampDeserializer()
            )
            addDeserializer(
                ChatId::class.java,
                ChatIdDeserializer()
            )
            addDeserializer(
                Attachment::class.java,
                AttachmentDeserializer()
            )
            addDeserializer(
                Duration::class.java,
                DurationInSecondsDeserializer()
            )
            addDeserializer(
                RgbColor::class.java,
                RgbColorDeserializer()
            )
        })
    }

}
