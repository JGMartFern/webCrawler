package com.example.webCrawler.service

import com.example.webCrawler.model.Entry
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class CrawlerServiceTest {

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
        println(doc)
        val service = CrawlerService()
        val entries = service.parseEntries(doc)

        assertEquals(2, entries.size)
        assertEquals(Entry(1, "Title 1", 100, 50), entries[0])
        assertEquals(Entry(2, "Title 2", 150, 75), entries[1])
    }

    @Test
    fun `should throw exception if it could not obtain any data`() {
        val service = CrawlerService()

        assertThrows<IOException> {
            service.fetchEntries("https://invalid.url")
        }
    }
}
