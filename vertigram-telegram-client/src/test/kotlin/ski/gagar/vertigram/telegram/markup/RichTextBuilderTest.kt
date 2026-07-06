package ski.gagar.vertigram.telegram.markup

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ski.gagar.vertigram.telegram.types.MessageEntity
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

object RichTextBuilderTest {
    private val unixTime = Instant.ofEpochSecond(1647531900)
    private val format = dateTimeFormat {
        weekday()
        longDate()
        longTime()
    }

    @Test
    fun `date time should render to markdown`() {
        assertEquals(
            "![22:45 tomorrow](tg://time?unix=1647531900&format=wDT)",
            textMarkdown {
                dateTime(text = "22:45 tomorrow", unixTime = unixTime, dateTimeFormat = format)
            }.text
        )
    }

    @Test
    fun `date time should render to html`() {
        assertEquals(
            """<tg-time unix="1647531900" format="wDT">22:45 tomorrow</tg-time>""",
            textHtml {
                dateTime(text = "22:45 tomorrow", unixTime = unixTime, dateTimeFormat = format)
            }.text
        )
    }

    @Test
    fun `date time should render to entity`() {
        assertEquals(
            MessageEntity.DateTime(
                offset = 0,
                length = 14,
                unixTime = unixTime,
                dateTimeFormat = "wDT"
            ),
            textWithEntities {
                dateTime(text = "22:45 tomorrow", unixTime = unixTime, dateTimeFormat = format)
            }.entities.single()
        )
    }

    @Test
    fun `date time should accept offset date time`() {
        assertEquals(
            """<tg-time unix="1647531900">22:45 tomorrow</tg-time>""",
            textHtml {
                dateTime(
                    text = "22:45 tomorrow",
                    dateTime = OffsetDateTime.ofInstant(unixTime, ZoneOffset.UTC)
                )
            }.text
        )
    }

    @Test
    fun `date time should accept zoned date time`() {
        assertEquals(
            "![22:45 tomorrow](tg://time?unix=1647531900&format=r)",
            textMarkdown {
                dateTime(
                    text = "22:45 tomorrow",
                    dateTime = ZonedDateTime.ofInstant(unixTime, ZoneOffset.UTC),
                    dateTimeFormat = DateTimeFormat.RELATIVE
                )
            }.text
        )
    }
}
