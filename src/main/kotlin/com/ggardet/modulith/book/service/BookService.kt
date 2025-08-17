package com.ggardet.modulith.book.service

import com.ggardet.modulith.author.api.AuthorDeleted
import com.ggardet.modulith.book.api.BookDeleted
import com.ggardet.modulith.book.mapper.BookMapper
import com.ggardet.modulith.book.model.Book
import com.ggardet.modulith.book.model.BookCreateRequest
import com.ggardet.modulith.book.model.BookUpdateRequest
import com.ggardet.modulith.book.repository.BookRepository
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class BookService(
    private val bookRepository: BookRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val bookMapper: BookMapper
) {
    companion object {
        private val logger = LoggerFactory.getLogger(BookService::class.java)
    }

    fun findBook(id: Long): Book? = bookRepository.findById(id).orElse(null)

    fun getBooks(pageable: Pageable): Page<Book> = bookRepository.findAll(pageable)

    @Transactional
    fun createBook(request: BookCreateRequest): Book {
        val book = bookMapper.toEntity(request)
        return bookRepository.save(book)
    }

    @Transactional
    fun updateBook(id: Long, request: BookUpdateRequest): Book {
        val book = findBook(id) ?: throw IllegalArgumentException("Book not found")
        val updatedBook = bookMapper.toUpdatedEntity(book, request)
        return bookRepository.save(updatedBook)
    }

    @Transactional
    fun deleteBook(id: Long) {
        val book = findBook(id) ?: throw IllegalArgumentException("Book not found")
        bookRepository.deleteById(id)
        eventPublisher.publishEvent(BookDeleted(book.id, book.title))
    }

    @EventListener
    @Transactional
    fun handleAuthorDeleted(event: AuthorDeleted) {
        val deletedCount = bookRepository.deleteByAuthorId(event.authorId)
        if (deletedCount > 0) {
            logger.info(
                "Deleted {} book(s) associated with author {} (ID: {})",
                deletedCount,
                event.name,
                event.authorId
            )
        }
    }
}
