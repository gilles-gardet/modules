package com.ggardet.modulith.book.controller

import com.ggardet.modulith.book.model.Book
import com.ggardet.modulith.book.model.BookCreateRequest
import com.ggardet.modulith.book.model.BookUpdateRequest
import com.ggardet.modulith.book.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid

@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService
) {

    @GetMapping
    fun getBooks(@PageableDefault(size = 20) pageable: Pageable): ResponseEntity<Page<Book>> {
        val books = bookService.getBooks(pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{id}")
    fun getBookById(@PathVariable id: Long): ResponseEntity<Book> {
        val book = bookService.findBook(id) ?: return ResponseEntity.notFound().build<Book>()
        return ResponseEntity.ok(book)
    }

    @PostMapping
    fun createBook(@Valid @RequestBody request: BookCreateRequest): ResponseEntity<*> {
        return try {
            val book = bookService.createBook(request)
            ResponseEntity.status(HttpStatus.CREATED).body(book)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build<Any>()
        }
    }

    @PutMapping("/{id}")
    fun updateBook(
        @PathVariable id: Long,
        @Valid @RequestBody request: BookUpdateRequest
    ): ResponseEntity<Book> {
        val book = bookService.updateBook(id, request)
        return ResponseEntity.ok(book)
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Any> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}
