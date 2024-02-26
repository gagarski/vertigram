package ski.gagar.vertigram.telegram.types

import org.junit.jupiter.api.Test
import ski.gagar.vertigram.BaseSerializationTest

object InlineQuerySerializationTest : BaseSerializationTest() {
    @Test
    fun `inline query result should survive serialization`() {
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Audio(
                id = "aaa",
                audioUrl = "https://example.com",
                title = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Audio.Cached(
                id = "aaa",
                audioFileId = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Article(
                id = "aaa",
                title = "aaa",
                inputMessageContent = InlineQuery.InputMessageContent.Text(messageText = "111")
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Contact(
                id = "aaa",
                phoneNumber = "+49-111-11-11-111",
                firstName = "XXX"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Document(
                id = "aaa",
                title = "xxx",
                documentUrl = "yyy",
                mimeType = "application/json"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Document.Cached(
                id = "aaa",
                title = "xxx",
                documentFileId = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Game(
                id = "aaa",
                gameShortName = "Game"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Gif(
                id = "aaa",
                gifUrl = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Gif.Cached(
                id = "aaa",
                gifFileId = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Location(
                id = "aaa",
                latitude = 1.0,
                longitude = 1.0,
                title = "Whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Mpeg4Gif(
                id = "aaa",
                mpeg4Url = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Mpeg4Gif.Cached(
                id = "aaa",
                mpeg4FileId = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Photo(
                id = "aaa",
                photoUrl = "https://example.com",
                thumbnailUrl = "https://example.com"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Photo.Cached(
                id = "aaa",
                photoFileId = "1"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Sticker.Cached(
                id = "aaa",
                stickerFileId = "1"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Venue(
                id = "aaa",
                latitude = 1.0,
                longitude = 1.0,
                title = "Whatever",
                address = "xxx"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Video(
                id = "aaa",
                videoUrl = "https://example.com",
                mimeType = "video/whatever",
                thumbnailUrl = "https://example.com",
                title = "aaa"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Video.Cached(
                id = "aaa",
                videoFileId = "1",
                title = "whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Voice(
                id = "aaa",
                voiceUrl = "http://example.com",
                title = "whatever"
            )
        )
        assertSerializable<InlineQuery.Result>(
            InlineQuery.Result.Voice.Cached(
                id = "aaa",
                voiceFileId = "1",
                title = "whatever"
            )
        )
    }

    @Test
    fun `input message content should survive serialization`() {
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Text(
                messageText = "xxx"
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Location(
                latitude = 1.0,
                longitude = 1.0
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Venue(
                latitude = 1.0,
                longitude = 1.0,
                address = "aaa",
                title = "bbb"
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Contact(
                firstName = "A",
                phoneNumber = "+1"
            )
        )
        assertSerializable<InlineQuery.InputMessageContent>(
            InlineQuery.InputMessageContent.Invoice(
                title = "aaa",
                description = "aaa",
                payload = "bbb",
                providerToken = "xxx",
                currency = "USD",
                prices = listOf(LabeledPrice(label = "xxx", amount = 1))
            )
        )

    }
}