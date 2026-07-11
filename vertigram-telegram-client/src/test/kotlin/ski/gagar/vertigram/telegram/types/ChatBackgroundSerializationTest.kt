package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.colors.RgbColor

object ChatBackgroundSerializationTest : BaseSerializationTest() {
    private val color = RgbColor.fromInt(0x112233)
    private val document = Document(
        fileId = "document",
        fileUniqueId = "unique-document"
    )

    @Test
    fun `chat background should survive serialization`() {
        assertSerializable<ChatBackground.Type>(
            ChatBackground.Type.Fill(
                fill = ChatBackground.Type.Fill.Value.Solid(color = color),
                darkThemeDimming = 1
            )
        )
        assertSerializable<ChatBackground.Type>(
            ChatBackground.Type.Wallpaper(
                document = document,
                darkThemeDimming = 1,
                isBlurred = true,
                isMoving = true
            )
        )
        assertSerializable<ChatBackground.Type>(
            ChatBackground.Type.Pattern(
                document = document,
                fill = ChatBackground.Type.Fill.Value.Gradient(
                    topColor = color,
                    bottomColor = RgbColor.fromInt(0x445566),
                    rotationAngle = 90
                ),
                intensity = 1,
                isInverted = true,
                isMoving = true
            )
        )
        assertSerializable<ChatBackground.Type>(
            ChatBackground.Type.ChatTheme(themeName = "theme")
        )
        assertSerializable<ChatBackground>(
            ChatBackground(
                type = ChatBackground.Type.Fill(
                    fill = ChatBackground.Type.Fill.Value.FreeformGradient(
                        colors = listOf(color, RgbColor.fromInt(0x445566))
                    ),
                    darkThemeDimming = 1
                )
            )
        )
    }

    @Test
    fun `background fill should survive serialization`() {
        assertSerializable<ChatBackground.Type.Fill.Value>(
            ChatBackground.Type.Fill.Value.Solid(color = color)
        )
        assertSerializable<ChatBackground.Type.Fill.Value>(
            ChatBackground.Type.Fill.Value.Gradient(
                topColor = color,
                bottomColor = RgbColor.fromInt(0x445566),
                rotationAngle = 90
            )
        )
        assertSerializable<ChatBackground.Type.Fill.Value>(
            ChatBackground.Type.Fill.Value.FreeformGradient(
                colors = listOf(color, RgbColor.fromInt(0x445566))
            )
        )
    }
}
