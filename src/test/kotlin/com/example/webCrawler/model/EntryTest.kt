package com.example.webCrawler.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EntryTest {
    @Test
    fun `should create Entry model with correct values`() {
        val entry = Entry(number = 1, title = "Test Title", points = 100, comments = 50)

        assertEquals(1, entry.number)
        assertEquals("Test Title", entry.title)
        assertEquals(100, entry.points)
        assertEquals(50, entry.comments)
    }
}