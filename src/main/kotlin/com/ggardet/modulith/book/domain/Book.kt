package com.ggardet.modulith.book.domain

import com.ggardet.modulith.author.domain.Author
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 200)
    val title: String,

    @Column(nullable = false, unique = true, length = 20)
    val isbn: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column
    val publishedDate: LocalDate? = null,

    @Column
    val pages: Int? = null,

    @Column(length = 100)
    val genre: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: Author,

    @Column(nullable = false)
    val available: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class BookPublicView(
    val id: Long,
    val title: String,
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    val authorName: String,
    val available: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(book: Book): BookPublicView {
            return BookPublicView(
                id = book.id,
                title = book.title,
                isbn = book.isbn,
                description = book.description,
                publishedDate = book.publishedDate,
                pages = book.pages,
                genre = book.genre,
                authorName = book.author.fullName,
                available = book.available,
                createdAt = book.createdAt
            )
        }
    }
}

data class BookCreateRequest(
    val title: String,
    val isbn: String,
    val description: String?,
    val publishedDate: LocalDate?,
    val pages: Int?,
    val genre: String?,
    val authorId: Long,
    val available: Boolean = true
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

data class AuthorCreateRequest(
    val firstName: String,
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)

data class AuthorUpdateRequest(
    val firstName: String,
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)
