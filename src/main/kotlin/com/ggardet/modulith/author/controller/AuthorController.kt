package com.ggardet.modulith.author.controller

import com.ggardet.modulith.author.model.Author
import com.ggardet.modulith.author.model.AuthorCreateRequest
import com.ggardet.modulith.author.model.AuthorUpdateRequest
import com.ggardet.modulith.author.service.AuthorService
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
@RequestMapping("/authors")
class AuthorController(private val authorService: AuthorService) {
    @GetMapping
    fun getAuthors(@PageableDefault(size = 20) pageable: Pageable): ResponseEntity<Page<Author>> {
        val authors = authorService.getAllAuthors(pageable)
        return ResponseEntity.ok(authors)
    }

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): ResponseEntity<Author> {
        val author = authorService.findById(id)?: return ResponseEntity.notFound().build<Author>()
        return ResponseEntity.ok(author)
    }

    @PostMapping
    fun createAuthor(@Valid @RequestBody request: AuthorCreateRequest): ResponseEntity<Author> {
        val author = authorService.createAuthor(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(author)
    }

    @PutMapping("/{id}")
    fun updateAuthor(
        @PathVariable id: Long,
        @Valid @RequestBody request: AuthorUpdateRequest
    ): ResponseEntity<Author> {
        val author = authorService.updateAuthor(id, request)
        return ResponseEntity.ok(author)
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(@PathVariable id: Long): ResponseEntity<Any> {
        authorService.deleteAuthor(id)
        return ResponseEntity.noContent().build()
    }
}
