package com.example.webCrawler.controller

import com.example.webCrawler.model.Entry
import com.example.webCrawler.service.CrawlerService
import org.jsoup.Jsoup
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CrawlerController(private val crawlerService: CrawlerService) {

    @GetMapping("/api/crawler/all")
    fun getAllEntries(@RequestParam url: String): List<Entry> {
        val document = Jsoup.connect(url).get()
        return crawlerService.parseEntries(document)
    }

    @GetMapping("/api/crawler/filter/long-titles")
    fun getFilteredEntriesWithLongTitles(@RequestParam url: String): List<Entry> {
        val document = Jsoup.connect(url).get()
        val entries = crawlerService.parseEntries(document)
        return crawlerService.filterByTitleLengthAndComments(entries)
    }

    @GetMapping("/api/crawler/filter/short-titles")
    fun getFilteredEntriesWithShortTitles(@RequestParam url: String): List<Entry> {
        val document = Jsoup.connect(url).get()
        val entries = crawlerService.parseEntries(document)
        return crawlerService.filterByTitleLengthAndPoints(entries)
    }
}
