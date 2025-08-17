package com.ggardet.modulith.book.mapper

import com.ggardet.modulith.book.model.Book
import com.ggardet.modulith.book.model.BookCreateRequest
import com.ggardet.modulith.book.model.BookUpdateRequest
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BookMapper {

    fun toEntity(request: BookCreateRequest): Book {
        return Book(
            title = request.title,
            isbn = request.isbn,
            description = request.description,
            publishedDate = request.publishedDate,
            pages = request.pages,
            genre = request.genre,
            authorId = request.authorId,
            available = request.available
        )
    }

    fun toUpdatedEntity(existing: Book, request: BookUpdateRequest): Book {
        return existing.copy(
            title = request.title,
            isbn = request.isbn,
            description = request.description,
            publishedDate = request.publishedDate,
            pages = request.pages,
            genre = request.genre,
            available = request.available,
            updatedAt = LocalDateTime.now()
        )
    }
}