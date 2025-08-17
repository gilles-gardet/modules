package com.ggardet.modulith.author.controller

import com.ggardet.modulith.author.service.AuthorService
import com.ggardet.modulith.book.domain.AuthorCreateRequest
import com.ggardet.modulith.book.domain.AuthorUpdateRequest
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for author management operations.
 *
 * Provides CRUD operations for authors including:
 * - Listing all authors with pagination
 * - Searching authors by name or nationality
 * - Creating, updating and deleting authors
 * - Filtering authors by birth year range
 */
@RestController
@RequestMapping("/api/authors")
class AuthorController(
    private val authorService: AuthorService
) {

    @GetMapping
    fun getAllAuthors(@PageableDefault(size = 20) pageable: Pageable): ResponseEntity<*> {
        val authors = authorService.getAllAuthors(pageable)
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): ResponseEntity<*> {
        val author = authorService.findById(id)
        return if (author != null) {
            ResponseEntity.ok(author)
        } else {
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping("/search")
    fun searchAuthors(
        @RequestParam name: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val authors = authorService.searchByName(name, pageable)
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/nationality/{nationality}")
    fun getAuthorsByNationality(
        @PathVariable nationality: String,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val authors = authorService.getByNationality(nationality, pageable)
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/birth-year")
    fun getAuthorsByBirthYearRange(
        @RequestParam startYear: Int,
        @RequestParam endYear: Int,
        @PageableDefault(size = 20) pageable: Pageable
    ): ResponseEntity<*> {
        val authors = authorService.getByBirthYearRange(startYear, endYear, pageable)
        return ResponseEntity.ok(authors)
    }

    @PostMapping
    fun createAuthor(@RequestBody request: AuthorCreateRequest): ResponseEntity<*> {
        return try {
            val author = authorService.createAuthor(request)
            ResponseEntity.status(HttpStatus.CREATED).body(author)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateAuthor(
        @PathVariable id: Long,
        @RequestBody request: AuthorUpdateRequest
    ): ResponseEntity<*> {
        return try {
            val author = authorService.updateAuthor(id, request)
            ResponseEntity.ok(author)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<*> {
        return try {
            authorService.deleteAuthor(id)
            ResponseEntity.noContent().build<Any>()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
