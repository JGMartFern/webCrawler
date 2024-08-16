# Web Crawler in Kotlin with Jsoup

## Description
This project is a web crawler application written in Kotlin, designed to extract data from a website.
In our case, we take the data from Hacker News by default, using the Jsoup library. However, the urls for the website
we want the data from can be provided manually as a parameter in the GET request. We just have to assume that the html
structure is like that of Hacker News, or adapt our CrawlerService and its tests for this purpose.

## Technologies Used
- **Language:** Kotlin
- **Libraries:** Jsoup, JUnit
- **Tools:** Gradle

## Project Structure
- **`model/Entry.kt`**: Model for showing the data we fetch and parse.
- **`model/EntryTest.kt`**: Unit tests for checking correct functioning of the model.
- **`service/CrawlerService.kt`**: Main web crawler logic.
- **`service/CrawlerServiceTest.kt`**: Unit tests to verify the scraping functionality.
- **`controller/CrawlerController.kt`**: Configuration for our GET requests so they show what we want and how we want.
- **`controller/CrawlerControllerTest.kt`**: Unit tests mocking some content and checking our use of the controller.

## Installation
1. Clone the repository.
2. Run `gradle build` to build the project.
3. Run `gradle test` to execute the unit tests.
4. With the project open, right click on WebCrawlerApplication.kt and click on Run to start the application.
5. Once the application is running, you can use this url in your browser: http://localhost:8080/api/crawler/all. It will
provide us the first 30 entries from https://news.ycombinator.com/
6. Alternatively, we can use http://localhost:8080/api/crawler/filter/long-titles. It will filter all previous entries
with more than five words in the title ordered by the number of comments first.
7. As a third option, http://localhost:8080/api/crawler/filter/short-titles will filter all entries from the point 5 
with less than or equal to five words in the title ordered by points.

## How It Works
The service uses Jsoup to parse HTML, selecting and extracting the relevant data: number, title, points, and comments.

## Future Improvements
- Support for dynamic HTML using Selenium.
- Further optimization of selectors to improve parsing efficiency and flexibility, making it adaptable to other
websites.
- Create more tests other than those that follow the happy path

## Author
- **Juan Gabriel Martinez**