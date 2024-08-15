package com.example.webCrawler.controller

import com.example.webCrawler.model.Entry
import com.example.webCrawler.service.CrawlerService
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(CrawlerController::class)
class CrawlerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var crawlerService: CrawlerService

    @Test
    fun `should return all entries`() {
        val mockEntries = listOf(
            Entry(number = 1, title = "Title 1", points = 100, comments = 50),
            Entry(number = 2, title = "Title 2", points = 150, comments = 75)
        )

        `when`(crawlerService.parseEntries(any())).thenReturn(mockEntries)

        mockMvc.perform(get("/api/crawler/all")
            .param("url", "http://example.com"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<Entry>(2)))
            .andExpect(jsonPath("$[0].title", `is`("Title 1")))
            .andExpect(jsonPath("$[1].title", `is`("Title 2")))
    }

    @Test
    fun `should return filtered entries with long titles`() {
        val mockEntries = listOf(
            Entry(number = 1, title = "This is a long title", points = 100, comments = 50),
            Entry(number = 2, title = "Another long title", points = 150, comments = 75)
        )

        val filteredEntries = listOf(mockEntries[0])

        `when`(crawlerService.parseEntries(any())).thenReturn(mockEntries)
        `when`(crawlerService.filterByTitleLengthAndComments(mockEntries)).thenReturn(filteredEntries)

        mockMvc.perform(get("/api/crawler/filter/long-titles")
            .param("url", "http://example.com"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<Entry>(1)))
            .andExpect(jsonPath("$[0].title", `is`("This is a long title")))
    }

    @Test
    fun `should return filtered entries with short titles`() {
        val mockEntries = listOf(
            Entry(number = 1, title = "Short", points = 100, comments = 50),
            Entry(number = 2, title = "Tiny", points = 150, comments = 75)
        )

        val filteredEntries = listOf(mockEntries[1])

        `when`(crawlerService.parseEntries(any())).thenReturn(mockEntries)
        `when`(crawlerService.filterByTitleLengthAndPoints(mockEntries)).thenReturn(filteredEntries)

        mockMvc.perform(get("/api/crawler/filter/short-titles")
            .param("url", "http://example.com"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize<Entry>(1)))
            .andExpect(jsonPath("$[0].title", `is`("Tiny")))
    }
}