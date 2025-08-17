package com.ggardet.modulith.book

import com.ggardet.modulith.author.model.Author
import com.ggardet.modulith.author.repository.AuthorRepository
import com.ggardet.modulith.book.model.Book
import com.ggardet.modulith.book.repository.BookRepository
import com.ggardet.modulith.config.CleanupTestData
import com.ggardet.modulith.config.TestConfig
import com.ggardet.modulith.core.event.EventConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Import(value = [TestConfig::class, EventConfig::class])
@Testcontainers
@CleanupTestData
class BookRepositoryTest {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Test
    @Transactional
    @Commit
    fun `should persist a book`() {
        val uniqueId = System.currentTimeMillis()
        val author = authorRepository.save(
            Author(
                firstName = "BookTest",
                lastName = "Author$uniqueId",
                nationality = "TestCountry",
                birthYear = 1985,
                biography = "Author created for Book TestContainers demo"
            )
        )
        val book = bookRepository.save(
            Book(
                title = "TestContainers Book $uniqueId",
                isbn = "978-0-${uniqueId.toString().takeLast(10).padStart(10, '0')}-1",
                description = "Book created for TestContainers demonstration",
                publishedDate = LocalDate.now(),
                pages = 250,
                genre = "TestGenre",
                authorId = author.id,
                available = true
            )
        )
        assertNotNull(book.id)
        assertTrue(book.id > 0)
        assertNotNull(author.id)
        assertTrue(author.id > 0)
        val foundBook = bookRepository.findById(book.id).orElse(null)
        assertNotNull(foundBook)
        assertEquals("TestContainers Book $uniqueId", foundBook.title)
        assertEquals(author.id, foundBook.authorId)
    }
}
