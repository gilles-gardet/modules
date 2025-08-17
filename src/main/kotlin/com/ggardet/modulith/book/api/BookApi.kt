package com.ggardet.modulith.book.api

import java.time.LocalDate

data class BookCreateRequest(
    val title: String,
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    val authorId: Long,
    val available: Boolean = true,
)

data class BookUpdateRequest(
    val title: String,
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    val authorId: Long,
    val available: Boolean
)

data class BookDeleted(val bookId: Long, val title: String)
