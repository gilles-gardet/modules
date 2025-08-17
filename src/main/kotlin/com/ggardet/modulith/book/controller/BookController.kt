package com.ggardet.modulith.book.controller

import com.ggardet.modulith.book.domain.BookCreateRequest
import com.ggardet.modulith.book.domain.BookUpdateRequest
import com.ggardet.modulith.book.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for book management operations.
 * 
 * Provides CRUD operations for books including:
 * - Listing all books with pagination
 * - Searching books by title, author, genre
 * - Creating, updating and deleting books
 * - Getting available books only
 */
@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping
    fun getAllBooks(@PageableDefault(size = 20) pageable: Pageable): ResponseEntity<*> {
        val books = bookService.getAllBooks(pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/available")
    fun getAvailableBooks(@PageableDefault(size = 20) pageable: Pageable): ResponseEntity<*> {
        val books = bookService.getAvailableBooks(pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): ResponseEntity<*> {
        val book = bookService.findById(id)
        return if (book != null) {
            ResponseEntity.ok(book)
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping("/isbn/{isbn}")
    fun getBookByIsbn(@PathVariable isbn: String): ResponseEntity<*> {
        val book = bookService.findByIsbn(isbn)
        return if (book != null) {
            ResponseEntity.ok(book)
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping("/search")
    fun searchBooks(
        @RequestParam title: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val books = bookService.searchByTitle(title, pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/author/{authorId}")
    fun getBooksByAuthor(
        @PathVariable authorId: Long,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val books = bookService.getBooksByAuthor(authorId, pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/genre/{genre}")
    fun getBooksByGenre(
        @PathVariable genre: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val books = bookService.getBooksByGenre(genre, pageable)
        return ResponseEntity.ok(books)
    }

    @PostMapping
    fun createBook(@RequestBody request: BookCreateRequest): ResponseEntity<*> {
        return try {
            val book = bookService.createBook(request)
            ResponseEntity.status(HttpStatus.CREATED).body(book)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @RequestBody request: BookUpdateRequest
    ): ResponseEntity<*> {
        return try {
            val book = bookService.updateBook(id, request)
            ResponseEntity.ok(book)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            bookService.deleteBook(id)
            ResponseEntity.noContent().build<Any>()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}