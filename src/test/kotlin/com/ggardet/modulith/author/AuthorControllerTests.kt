package com.ggardet.modulith.author

import com.ggardet.modulith.author.model.AuthorCreateRequest
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

@ApplicationModuleTest(extraIncludes = ["core"], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(value = [EventConfig::class])
class AuthorControllerTests {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.authentication = RestAssured.basic("admin", "password")
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun `should create, read, update and delete author successfully`() {
        val createRequest = AuthorCreateRequest(
            firstName = "Isaac",
            lastName = "Asimov",
            nationality = "American",
            birthYear = 1920,
            biography = "Isaac Asimov was an American writer and professor of biochemistry at Boston University."
        )
        val authorId = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(createRequest)
            .`when`()
            .post("/api/authors")
            .then()
            .statusCode(201)
            .body("firstName", equalTo("Isaac"))
            .body("lastName", equalTo("Asimov"))
            .body("nationality", equalTo("American"))
            .body("birthYear", equalTo(1920))
            .body(
                "biography",
                equalTo("Isaac Asimov was an American writer and professor of biochemistry at Boston University.")
            )
            .body("id", notNullValue())
            .extract()
            .path<Int>("id")
            .toLong()
        RestAssured.given()
            .`when`()
            .get("/api/authors/{id}", authorId)
            .then()
            .statusCode(200)
            .body("id", equalTo(authorId.toInt()))
            .body("firstName", equalTo("Isaac"))
            .body("lastName", equalTo("Asimov"))
        val updateRequest = AuthorCreateRequest(
            firstName = "Isaac",
            lastName = "Asimov",
            nationality = "Russian-American",
            birthYear = 1920,
            biography = "Isaac Asimov was a Russian-American writer and professor of biochemistry, known for his works of science fiction and popular science."
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .`when`()
            .put("/api/authors/{id}", authorId)
            .then()
            .statusCode(200)
            .body("nationality", equalTo("Russian-American"))
            .body(
                "biography",
                equalTo("Isaac Asimov was a Russian-American writer and professor of biochemistry, known for his works of science fiction and popular science.")
            )
        RestAssured.given()
            .`when`()
            .get("/api/authors")
            .then()
            .statusCode(200)
            .body("content", hasSize<Any>(greaterThan(0)))
            .body("content.find { it.id == $authorId }.firstName", equalTo("Isaac"))
        RestAssured.given()
            .`when`()
            .delete("/api/authors/{id}", authorId)
            .then()
            .statusCode(204)
        RestAssured.given()
            .`when`()
            .get("/api/authors/{id}", authorId)
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return 404 when getting non-existent author`() {
        RestAssured.given()
            .`when`()
            .get("/api/authors/999999999")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should return paginated authors list`() {
        RestAssured.given()
            .queryParam("page", 0)
            .queryParam("size", 5)
            .queryParam("sort", "lastName,asc")
            .`when`()
            .get("/api/authors")
            .then()
            .statusCode(200)
            .body("content", hasSize<Any>(lessThanOrEqualTo(5)))
            .body("pageable.pageSize", equalTo(5))
            .body("pageable.pageNumber", equalTo(0))
            .body("sort.sorted", equalTo(true))
    }

    @Test
    fun `should validate required fields when creating author`() {
        val invalidRequest = AuthorCreateRequest(
            firstName = "",
            lastName = "",
            nationality = null,
            birthYear = null,
            biography = null
        )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
            .`when`()
            .post("/api/authors")
            .then()
            .statusCode(400)
    }
}
