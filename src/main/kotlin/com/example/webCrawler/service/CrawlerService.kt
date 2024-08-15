package com.example.webCrawler.service

import com.example.webCrawler.model.Entry
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class CrawlerService {

    @Throws(IOException::class)
    fun fetchEntries(url: String = "https://news.ycombinator.com/"): List<Entry> {
        val doc = Jsoup.connect(url).get()
        return parseEntries(doc)
    }

    fun parseEntries(doc: Document): List<Entry> {
        val entries = mutableListOf<Entry>()
        val rows = doc.select("tr.athing")

        for (row in rows) {
            val number = row.selectFirst("span.rank")?.text()?.replace(".", "")?.toInt() ?: continue
            val title = row.selectFirst("td.title > a")?.text() ?: continue
            val points =
                row.nextElementSibling()?.selectFirst("span.score")?.text()?.replace(" points", "")?.toInt() ?: 0
            val comments =
                row.nextElementSibling()?.select("a")?.last()?.text()?.replace(" comments", "")?.toIntOrNull() ?: 0

            entries.add(Entry(number, title, points, comments))
        }

        return entries.take(30)
    }

    fun filterByTitleLengthAndComments(entries: List<Entry>): List<Entry> {
        return entries.filter { it.title.wordCount() > 5 }
            .sortedByDescending { it.comments }
    }

    fun filterByTitleLengthAndPoints(entries: List<Entry>): List<Entry> {
        return entries.filter { it.title.wordCount() <= 5 }
            .sortedByDescending { it.points }
    }

    private fun String.wordCount(): Int {
        return this.split("\\s+".toRegex()).count { it.matches("\\w+".toRegex()) }
    }
}