package com.ggardet.modulith.book.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 200)
    val title: String = "",

    @Column(nullable = false, unique = true, length = 20)
    val isbn: String = "",

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column
    val publishedDate: LocalDate? = null,

    @Column
    val pages: Int? = null,

    @Column(length = 100)
    val genre: String? = null,

    @Column(name = "author_id", nullable = false)
    val authorId: Long = 0,

    @Column(nullable = false)
    val available: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
