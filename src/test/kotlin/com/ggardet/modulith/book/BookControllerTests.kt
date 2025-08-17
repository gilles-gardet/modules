package com.ggardet.modulith.book

import com.ggardet.modulith.book.model.BookCreateRequest
import com.ggardet.modulith.core.event.EventConfig
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.modulith.test.ApplicationModuleTest
import java.time.LocalDate

@ApplicationModuleTest(extraIncludes = ["core"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = [EventConfig::class])
class BookControllerTests {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.authentication = RestAssured.basic("admin", "password")
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun `should create, read, update and delete book successfully`() {
        val createRequest = BookCreateRequest(
            title = "The Foundation",
            isbn = "978-0-553-29335-0",
            description = "Foundation is a science fiction novel by American writer Isaac Asimov.",
            publishedDate = LocalDate.of(1951, 5, 1),
            pages = 244,
            genre = "Science Fiction",
            authorId = 1L,
            available = true
        )
        val bookId = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .`when`()
            .post("/api/books")
            .then()
            .statusCode(201)
            .body("title", equalTo("The Foundation"))
            .body("isbn", equalTo("978-0-553-29335-0"))
            .body("description", equalTo("Foundation is a science fiction novel by American writer Isaac Asimov."))
            .body("publishedDate", equalTo("1951-05-01"))
            .body("pages", equalTo(244))
            .body("genre", equalTo("Science Fiction"))
            .body("authorId", equalTo(1))
            .body("available", equalTo(true))
            .body("id", notNullValue())
            .extract()
            .path<Int>("id")
            .toLong()
        RestAssured.given()
            .`when`()
            .get("/api/books/{id}", bookId)
            .then()
            .statusCode(200)
            .body("id", equalTo(bookId.toInt()))
            .body("title", equalTo("The Foundation"))
            .body("isbn", equalTo("978-0-553-29335-0"))
        val updateRequest = BookCreateRequest(
            title = "Foundation (Updated Edition)",
            isbn = "978-0-553-29335-0",
            description = "Foundation is a science fiction novel by American writer Isaac Asimov. This is the updated edition with additional notes.",
            publishedDate = LocalDate.of(1951, 5, 1),
            pages = 280,
            genre = "Science Fiction",
            authorId = 1L,
            available = false
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`()
            .put("/api/books/{id}", bookId)
            .then()
            .statusCode(200)
            .body("title", equalTo("Foundation (Updated Edition)"))
            .body("pages", equalTo(280))
            .body("available", equalTo(false))
        RestAssured.given()
            .`when`()
            .get("/api/books")
            .then()
            .statusCode(200)
            .body("content", hasSize<Any>(greaterThan(0)))
        RestAssured.given()
            .`when`()
            .delete("/api/books/{id}", bookId)
            .then()
            .statusCode(204)
        RestAssured.given()
            .`when`()
            .get("/api/books/{id}", bookId)
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return 404 when getting non-existent book`() {
        RestAssured.given()
            .`when`()
            .get("/api/books/999999999")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return paginated books list`() {
        RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("sort", "title,asc")
            .`when`()
            .get("/api/books")
            .then()
            .statusCode(200)
            .body("content", hasSize<Any>(lessThanOrEqualTo(5)))
            .body("pageable.pageSize", equalTo(5))
            .body("pageable.pageNumber", equalTo(0))
            .body("sort.sorted", equalTo(true))
    }

    @Test
    fun `should validate required fields when creating book`() {
        val invalidRequest = BookCreateRequest(
            title = "",
            isbn = "",
            description = null,
            publishedDate = null,
            pages = null,
            genre = null,
            authorId = 1L,
            available = true
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
            .`when`()
            .post("/api/books")
            .then()
            .statusCode(400)
    }

    @Test
    fun `should fail when creating book with non-existent author`() {
        val requestWithInvalidAuthor = BookCreateRequest(
            title = "Test Book",
            isbn = "978-0-000-00000-0",
            description = "Test book description",
            publishedDate = LocalDate.now(),
            pages = 100,
            genre = "Test",
            authorId = 999999L,
            available = true
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestWithInvalidAuthor)
            .`when`()
            .post("/api/books")
            .then()
            .statusCode(400)
    }
}
