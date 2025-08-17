package com.ggardet.modulith.author.service

import com.ggardet.modulith.author.domain.Author
import com.ggardet.modulith.author.repository.AuthorRepository
import com.ggardet.modulith.book.domain.AuthorCreateRequest
import com.ggardet.modulith.book.domain.AuthorUpdateRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findById(id: Long): Author? = authorRepository.findById(id).orElse(null)

    fun getAllAuthors(pageable: Pageable): Page<Author> = authorRepository.findAll(pageable)

    fun searchByName(name: String, pageable: Pageable): Page<Author> =
        authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            name, name, pageable
        )

    fun getByNationality(nationality: String, pageable: Pageable): Page<Author> =
        authorRepository.findByNationality(nationality, pageable)

    fun getByBirthYearRange(startYear: Int, endYear: Int, pageable: Pageable): Page<Author> =
        authorRepository.findByBirthYearBetween(startYear, endYear, pageable)

    @Transactional
    fun createAuthor(request: AuthorCreateRequest): Author {
        val author = Author(
            firstName = request.firstName,
            lastName = request.lastName,
            nationality = request.nationality,
            birthYear = request.birthYear,
            biography = request.biography
        )

        val savedAuthor = authorRepository.save(author)
        eventPublisher.publishEvent(AuthorCreated(savedAuthor.id, savedAuthor.fullName))
        return savedAuthor
    }

    @Transactional
    fun updateAuthor(id: Long, request: AuthorUpdateRequest): Author {
        val author = findById(id) ?: throw IllegalArgumentException("Author not found")

        val updatedAuthor = author.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            nationality = request.nationality,
            birthYear = request.birthYear,
            biography = request.biography,
            updatedAt = LocalDateTime.now()
        )

        val savedAuthor = authorRepository.save(updatedAuthor)
        eventPublisher.publishEvent(AuthorUpdated(savedAuthor.id, savedAuthor.fullName))
        return savedAuthor
    }

    @Transactional
    fun deleteAuthor(id: Long) {
        val author = findById(id) ?: throw IllegalArgumentException("Author not found")
        
        // Check if author has books
        if (author.books.isNotEmpty()) {
            throw IllegalArgumentException("Cannot delete author with existing books")
        }
        
        authorRepository.deleteById(id)
        eventPublisher.publishEvent(AuthorDeleted(author.id, author.fullName))
    }
}

data class AuthorCreated(val authorId: Long, val name: String)
data class AuthorUpdated(val authorId: Long, val name: String)
data class AuthorDeleted(val authorId: Long, val name: String)
