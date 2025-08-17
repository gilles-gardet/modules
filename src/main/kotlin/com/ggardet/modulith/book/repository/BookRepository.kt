package com.ggardet.modulith.book.repository

import com.ggardet.modulith.book.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    fun deleteByAuthorId(authorId: Long): Int
}
