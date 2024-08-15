package com.example.webCrawler.service

import com.example.webCrawler.model.Entry
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class CrawlerServiceTest {

    val service = CrawlerService()

    @Test
    fun `should fetch and parse entries correctly`() {
        val html = """
        <table>
            <tr class='athing'>
                <td class='title'><span class='rank'>1.</span></td>
                <td class='title'><a href=''>Title 1</a></td>
            </tr>
            <tr>
                <td class='subtext'>
                    <span>
                        <span class='score'>100 points</span>
                        <a href=''>50 comments</a>
                    </span>
                </td>
            </tr>
            <tr class='athing'>
                <td class='title'><span class='rank'>2.</span></td>
                <td class='title'><a href=''>Title 2</a></td>
            </tr>
            <tr>
                <td class='subtext'>
                    <span>
                        <span class='score'>150 points</span>
                        <a href=''>75 comments</a>
                    </span>
                </td>
            </tr>
        </table>
    """.trimIndent()

        val doc = Jsoup.parse(html)
        val entries = service.parseEntries(doc)

        assertEquals(2, entries.size)
        assertEquals(Entry(1, "Title 1", 100, 50), entries[0])
        assertEquals(Entry(2, "Title 2", 150, 75), entries[1])
    }

    @Test
    fun `should throw exception if it could not obtain any data`() {

        assertThrows<IOException> {
            service.fetchEntries("https://invalid.url")
        }
    }

    @Test
    fun `should filter entries with more than 5 words in the title and sort by comments`() {
        val entries = listOf(
            Entry(1, "This is a short title *", 50, 100),
            Entry(2, "A much longer title that definitely has more than five words", 30, 200),
            Entry(3, "Short again - - - -", 20, 150),
            Entry(4, "Another title that exceeds five words easily", 40, 300)
        )

        val result = service.filterByTitleLengthAndComments(entries)

        assertEquals(2, result.size)
        assertEquals(Entry(4, "Another title that exceeds five words easily", 40, 300), result[0])
        assertEquals(Entry(2, "A much longer title that definitely has more than five words", 30, 200), result[1])
    }

    @Test
    fun `should filter entries with 5 or fewer words in the title and sort by points`() {
        val entries = listOf(
            Entry(1, "This is short", 100, 50),
            Entry(2, "This is a bit longer", 150, 30),
            Entry(3, "Short title", 200, 75),
            Entry(4, "Very short title totally made up", 50, 40)
        )

        val result = service.filterByTitleLengthAndPoints(entries)

        assertEquals(3, result.size)
        assertEquals(Entry(3, "Short title", 200, 75), result[0])
        assertEquals(Entry(2, "This is a bit longer", 150, 30), result[1])
        assertEquals(Entry(1, "This is short", 100, 50), result[2])
    }
}
