package com.example.webCrawler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebCrawlerApplication

fun main(args: Array<String>) {
	runApplication<WebCrawlerApplication>(*args)
}
