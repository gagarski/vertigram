package ski.gagar.vertigram.telegram.markup

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant

object RichMessageBuilderTest {
    @Test
    fun `rich message html builder should render rich blocks`() {
        val message = richMessageHtml(isRtl = true, skipEntityDetection = true) {
            h1 { +"Report" }
            paragraph {
                +"Hello "
                b { +"<world>" }
                +" "
                dateTime(
                    text = "now",
                    unixTime = Instant.ofEpochSecond(1),
                    dateTimeFormat = DateTimeFormat.RELATIVE
                )
            }
            unorderedList {
                item(checked = true) {
                    paragraph { +"done" }
                }
            }
            photo("https://example.com/photo?a=1&b=2") {
                +"Photo"
                credit { +"Author" }
            }
            table(bordered = true, caption = { +"Numbers" }) {
                row {
                    cell(header = true) { +"A" }
                    cell { +"1" }
                }
            }
        }

        assertEquals(
            """
                <h1>Report</h1><p>Hello <b>&lt;world&gt;</b> <tg-time unix="1" format="r">now</tg-time></p><ul><li><input type="checkbox" checked=""></input><p>done</p></li></ul><figure><img src="https://example.com/photo?a=1&amp;b=2"><figcaption>Photo<cite>Author</cite></figcaption></figure><table bordered=""><caption>Numbers</caption><tr><th>A</th><td>1</td></tr></table>
            """.trimIndent(),
            message.html
        )
        assertEquals(true, message.isRtl)
        assertEquals(true, message.skipEntityDetection)
    }

    @Test
    fun `rich message markdown builder should keep markdown body`() {
        assertEquals(
            "# Title",
            richMessageMarkdown("# Title").markdown
        )
    }

    @Test
    fun `rich message markdown builder should render rich blocks`() {
        val message = richMessageMarkdown {
            h1 { +"Report" }
            paragraph {
                +"Hello "
                b { +"world" }
            }
        }

        assertEquals(
            "# Report\n\nHello **world**",
            message.markdown
        )
    }
}
