package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.types.colors.RgbColor
import java.time.Instant
import java.time.temporal.ChronoUnit

object OwnedGiftSerializationTest : BaseSerializationTest() {
    private val STICKER = Sticker.create(
        fileId = "1",
        fileUniqueId = "1",
        type = Sticker.Type.REGULAR,
        width = 100,
        height = 100,
    )

    @Test
    fun `owned gift should survive serialization`() {
        assertSerializable<OwnedGift>(
            OwnedGift.Regular.create(
                gift = Gift.create(
                    id = "1",
                    sticker = STICKER,
                    starCount = 1,
                ),
                sendDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
            )
        )
        assertSerializable<OwnedGift>(
            OwnedGift.Unique.create(
                gift = UniqueGift.create(
                    baseName = "1",
                    name = "1",
                    number = 100,
                    model = UniqueGift.Model.create(
                        name = "1",
                        sticker = STICKER,
                        rarityPerMille = 1
                    ),
                    symbol = UniqueGift.Symbol.create(
                        name = "1",
                        sticker = STICKER,
                        rarityPerMille = 1
                    ),
                    backdrop = UniqueGift.Backdrop.create(
                        name = "1",
                        colors = UniqueGift.Backdrop.Colors.create(
                            centerColor = RgbColor(0U, 0U, 0U),
                            edgeColor = RgbColor(0U, 0U, 0U),
                            textColor = RgbColor(0U, 0U, 0U),
                            symbolColor = RgbColor(0U, 0U, 0U)
                        ),
                        rarityPerMille = 1
                    )
                ),
                sendDate = Instant.now().truncatedTo(ChronoUnit.SECONDS),
            )
        )
    }


}