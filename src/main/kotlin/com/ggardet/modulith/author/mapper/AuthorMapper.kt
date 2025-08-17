package com.ggardet.modulith.author.mapper

import com.ggardet.modulith.author.model.Author
import com.ggardet.modulith.author.model.AuthorCreateRequest
import com.ggardet.modulith.author.model.AuthorUpdateRequest
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AuthorMapper {

    fun toEntity(request: AuthorCreateRequest): Author {
        return Author(
            firstName = request.firstName,
            lastName = request.lastName,
            nationality = request.nationality,
            birthYear = request.birthYear,
            biography = request.biography
        )
    }

    fun toUpdatedEntity(existing: Author, request: AuthorUpdateRequest): Author {
        return existing.copy(
            firstName = request.firstName,
            lastName = request.lastName,
            nationality = request.nationality,
            birthYear = request.birthYear,
            biography = request.biography,
            updatedAt = LocalDateTime.now()
        )
    }

    fun getFullName(author: Author): String {
        return "${author.firstName} ${author.lastName}"
    }
}