package com.ggardet.modulith.author

import com.ggardet.modulith.author.model.Author
import com.ggardet.modulith.author.repository.AuthorRepository
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest
@Import(value = [TestConfig::class, EventConfig::class])
@Testcontainers
@CleanupTestData
class AuthorRepositoryTest {
    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Test
    @Transactional
    @Commit
    fun `should persist an Author`() {
        val beforeCount = authorRepository.count()
        val uniqueId = System.currentTimeMillis()
        val author = authorRepository.save(
            Author(
                firstName = "AuthorTest",
                lastName = "TestContainer$uniqueId",
                nationality = "TestNationality",
                birthYear = 1990,
                biography = "Author created for TestContainers demonstration in Author module"
            )
        )
        assertNotNull(author.id)
        assertTrue(author.id > 0)
        val foundAuthor = authorRepository.findById(author.id).orElse(null)
        assertNotNull(foundAuthor)
        assertEquals("AuthorTest", foundAuthor.firstName)
        assertEquals("TestContainer$uniqueId", foundAuthor.lastName)
        val afterCount = authorRepository.count()
        assertEquals(afterCount, beforeCount + 1)
    }
}
