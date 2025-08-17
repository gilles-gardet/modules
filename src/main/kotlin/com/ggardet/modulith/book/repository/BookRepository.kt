package com.ggardet.modulith.book.repository

import com.ggardet.modulith.book.domain.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    
    fun findByIsbn(isbn: String): Optional<Book>
    
    fun findByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Book>
    
    fun findByAuthorId(authorId: Long, pageable: Pageable): Page<Book>
    
    @Query("SELECT b FROM Book b WHERE b.available = true")
    fun findAllAvailable(pageable: Pageable): Page<Book>
    
    @Query("SELECT b FROM Book b WHERE b.genre = :genre")
    fun findByGenre(genre: String, pageable: Pageable): Page<Book>
}