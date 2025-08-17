package com.ggardet.modulith.book.service

import com.ggardet.modulith.book.domain.Book
import com.ggardet.modulith.book.domain.BookCreateRequest
import com.ggardet.modulith.book.domain.BookPublicView
import com.ggardet.modulith.book.domain.BookUpdateRequest
import com.ggardet.modulith.author.repository.AuthorRepository
import com.ggardet.modulith.book.repository.BookRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun findById(id: Long): Book? = bookRepository.findById(id).orElse(null)

    fun findByIsbn(isbn: String): Book? = bookRepository.findByIsbn(isbn).orElse(null)

    fun getAllBooks(pageable: Pageable): Page<BookPublicView> =
        bookRepository.findAll(pageable).map { BookPublicView.from(it) }

    fun getAvailableBooks(pageable: Pageable): Page<BookPublicView> =
        bookRepository.findAllAvailable(pageable).map { BookPublicView.from(it) }

    fun searchByTitle(title: String, pageable: Pageable): Page<BookPublicView> =
        bookRepository.findByTitleContainingIgnoreCase(title, pageable).map { BookPublicView.from(it) }

    fun getBooksByAuthor(authorId: Long, pageable: Pageable): Page<BookPublicView> =
        bookRepository.findByAuthorId(authorId, pageable).map { BookPublicView.from(it) }

    fun getBooksByGenre(genre: String, pageable: Pageable): Page<BookPublicView> =
        bookRepository.findByGenre(genre, pageable).map { BookPublicView.from(it) }

    @Transactional
    fun createBook(request: BookCreateRequest): Book {
        val author = authorRepository.findById(request.authorId).orElseThrow {
            IllegalArgumentException("Author not found")
        }
        
        if (bookRepository.findByIsbn(request.isbn).isPresent) {
            throw IllegalArgumentException("A book with ISBN ${request.isbn} already exists")
        }

        val book = Book(
            title = request.title,
            isbn = request.isbn,
            description = request.description,
            publishedDate = request.publishedDate,
            pages = request.pages,
            genre = request.genre,
            author = author,
            available = request.available
        )

        val savedBook = bookRepository.save(book)
        eventPublisher.publishEvent(BookCreated(savedBook.id, savedBook.title, savedBook.author.id))
        return savedBook
    }

    @Transactional
    fun updateBook(id: Long, request: BookUpdateRequest): Book {
        val book = findById(id) ?: throw IllegalArgumentException("Book not found")
        val author = authorRepository.findById(request.authorId).orElseThrow {
            IllegalArgumentException("Author not found")
        }

        val existingBookWithIsbn = bookRepository.findByIsbn(request.isbn)
        if (existingBookWithIsbn.isPresent && existingBookWithIsbn.get().id != id) {
            throw IllegalArgumentException("A book with ISBN ${request.isbn} already exists")
        }

        val updatedBook = book.copy(
            title = request.title,
            isbn = request.isbn,
            description = request.description,
            publishedDate = request.publishedDate,
            pages = request.pages,
            genre = request.genre,
            author = author,
            available = request.available,
            updatedAt = LocalDateTime.now()
        )

        val savedBook = bookRepository.save(updatedBook)
        eventPublisher.publishEvent(BookUpdated(savedBook.id, savedBook.title))
        return savedBook
    }

    @Transactional
    fun deleteBook(id: Long) {
        val book = findById(id) ?: throw IllegalArgumentException("Book not found")
        bookRepository.deleteById(id)
        eventPublisher.publishEvent(BookDeleted(book.id, book.title))
    }
}

data class BookCreated(val bookId: Long, val title: String, val authorId: Long)
data class BookUpdated(val bookId: Long, val title: String)
data class BookDeleted(val bookId: Long, val title: String)
