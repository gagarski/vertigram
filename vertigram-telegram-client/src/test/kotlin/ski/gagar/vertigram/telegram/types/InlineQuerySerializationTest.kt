package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest
import ski.gagar.vertigram.telegram.markup.toRichText

object InlineQuerySerializationTest : BaseSerializationTest() {
    @Test
    fun `inline query result should survive serialization`() {
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Audio.create(
                id = "aaa",
                audioUrl = "https://example.com",
                title = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Audio.Cached.create(
                id = "aaa",
                audioFileId = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Article.create(
                id = "aaa",
                title = "aaa",
                inputMessageContent = InlineQuery.InputMessageContent.Text(messageText = "111")
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Contact.create(
                id = "aaa",
                phoneNumber = "+49-111-11-11-111",
                firstName = "XXX"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Document.create(
                id = "aaa",
                title = "xxx",
                documentUrl = "yyy",
                mimeType = "application/json"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Document.Cached.create(
                id = "aaa",
                title = "xxx",
                documentFileId = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Game.create(
                id = "aaa",
                gameShortName = "Game"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Gif.create(
                id = "aaa",
                gifUrl = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Gif.Cached.create(
                id = "aaa",
                gifFileId = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Location.create(
                id = "aaa",
                latitude = 1.0,
                longitude = 1.0,
                title = "Whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Mpeg4Gif.create(
                id = "aaa",
                mpeg4Url = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Mpeg4Gif.Cached.create(
                id = "aaa",
                mpeg4FileId = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Photo.create(
                id = "aaa",
                photoUrl = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Photo.Cached.create(
                id = "aaa",
                photoFileId = "1"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Sticker.Cached.create(
                id = "aaa",
                stickerFileId = "1"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Venue.create(
                id = "aaa",
                latitude = 1.0,
                longitude = 1.0,
                title = "Whatever",
                address = "xxx"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Video.create(
                id = "aaa",
                videoUrl = "https://example.com",
                mimeType = "video/whatever",
                thumbnailUrl = "https://example.com",
                title = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Video.Cached.create(
                id = "aaa",
                videoFileId = "1",
                title = "whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Voice.create(
                id = "aaa",
                voiceUrl = "http://example.com",
                title = "whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Voice.Cached.create(
                id = "aaa",
                voiceFileId = "1",
                title = "whatever"
            )
        )
    }

    @Test
    fun `input message content should survive serialization`() {
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Text.create(
                richMessageText = "xxx".toRichText()
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Location.create(
                latitude = 1.0,
                longitude = 1.0
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Venue.create(
                latitude = 1.0,
                longitude = 1.0,
                address = "aaa",
                title = "bbb"
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Contact.create(
                firstName = "A",
                phoneNumber = "+1"
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Invoice.create(
                title = "aaa",
                description = "aaa",
                payload = "bbb",
                providerToken = "xxx",
                currency = "USD",
                prices = listOf(LabeledPrice.create(label = "xxx", amount = 1))
            )
        )

    }
}