package com.example.webCrawler.controller

import com.example.webCrawler.model.Entry
import com.example.webCrawler.service.CrawlerService
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CrawlerController(private val crawlerService: CrawlerService) {

    val defaultUrl = "https://news.ycombinator.com/"

    @GetMapping("/api/crawler/all")
    fun getAllEntries(@RequestParam url: String = defaultUrl): List<Entry> {
        val document = Jsoup.connect(url).get()
        return crawlerService.parseEntries(document)
    }

    @GetMapping("/api/crawler/filter/long-titles")
    fun getFilteredEntriesWithLongTitles(@RequestParam url: String = defaultUrl): List<Entry> {
        val document = Jsoup.connect(url).get()
        val entries = crawlerService.parseEntries(document)
        return crawlerService.filterByLongTitleAndComments(entries)
    }

    @GetMapping("/api/crawler/filter/short-titles")
    fun getFilteredEntriesWithShortTitles(@RequestParam url: String = defaultUrl): List<Entry> {
        val document = Jsoup.connect(url).get()
        val entries = crawlerService.parseEntries(document)
        return crawlerService.filterByShortTitleAndPoints(entries)
    }
}
