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
        <tr class='athing' id='41255456'>
            <td align="right" valign="top" class="title"><span class="rank">1.</span></td>
            <td valign="top" class="votelinks"><center><a id='up_41255456' href='vote?id=41255456&amp;how=up&amp;goto=news'><div class='votearrow' title='upvote'></div></a></center></td>
            <td class="title"><span class="titleline"><a href="https://golem.ph.utexas.edu/category/2024/08/galois_theory.html">Galois Theory</a><span class="sitebit comhead"> (<a href="from?site=utexas.edu"><span class="sitestr">utexas.edu</span></a>)</span></span></td>
        </tr>
        <tr>
            <td colspan="2"></td>
            <td class="subtext">
                <span class="subline">
                    <span class="score" id="score_41255456">173 points</span> by <a href="user?id=mathgenius" class="hnuser">mathgenius</a>
                    <span class="age" title="2024-08-15T13:00:48"><a href="item?id=41255456">5 hours ago</a></span>
                    <span id="unv_41255456"></span> | <a href="hide?id=41255456&amp;goto=news">hide</a> | <a href="item?id=41255456">81&nbsp;comments</a>
                </span>
            </td>
        </tr>
        <tr class='athing' id='41257369'>
            <td align="right" valign="top" class="title"><span class="rank">2.</span></td>
            <td valign="top" class="votelinks"><center><a id='up_41257369' href='vote?id=41257369&amp;how=up&amp;goto=news'><div class='votearrow' title='upvote'></div></a></center></td>
            <td class="title"><span class="titleline"><a href="item?id=41257369">Launch HN: Hamming (YC S24) – Automated Testing for Voice Agents</a></span></td>
        </tr>
        <tr>
            <td colspan="2"></td>
            <td class="subtext">
                <span class="subline">
                    <span class="score" id="score_41257369">34 points</span> by <a href="user?id=sumanyusharma" class="hnuser">sumanyusharma</a>
                    <span class="age" title="2024-08-15T15:44:53"><a href="item?id=41257369">2 hours ago</a></span>
                    <span id="unv_41257369"></span> | <a href="hide?id=41257369&amp;goto=news">hide</a> | <a href="item?id=41257369">15&nbsp;comments</a>
                </span>
            </td>
        </tr>
    </table>
    """.trimIndent()

        val doc = Jsoup.parse(html)
        val entries = service.parseEntries(doc)

        assertEquals(2, entries.size)
        assertEquals(Entry(1, "Galois Theory", 173, 81), entries[0])
        assertEquals(Entry(2, "Launch HN: Hamming (YC S24) – Automated Testing for Voice Agents", 34, 15), entries[1])
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

        val result = service.filterByLongTitleAndComments(entries)

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

        val result = service.filterByShortTitleAndPoints(entries)

        assertEquals(3, result.size)
        assertEquals(Entry(3, "Short title", 200, 75), result[0])
        assertEquals(Entry(2, "This is a bit longer", 150, 30), result[1])
        assertEquals(Entry(1, "This is short", 100, 50), result[2])
    }
}
