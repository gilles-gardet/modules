package com.ggardet.modulith.book.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class BookCreateRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    @field:NotBlank(message = "ISBN is required")
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    @field:NotNull(message = "Author ID is required")
    val authorId: Long,
    val available: Boolean = true,
)

data class BookUpdateRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
    @field:NotBlank(message = "ISBN is required")
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    @field:NotNull(message = "Author ID is required")
    val authorId: Long,
    val available: Boolean
)
