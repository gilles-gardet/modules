package com.ggardet.modulith.author.service

import com.ggardet.modulith.author.api.AuthorCreated
import com.ggardet.modulith.author.api.AuthorDeleted
import com.ggardet.modulith.author.api.AuthorUpdated
import com.ggardet.modulith.author.mapper.AuthorMapper
import com.ggardet.modulith.author.model.Author
import com.ggardet.modulith.author.model.AuthorCreateRequest
import com.ggardet.modulith.author.model.AuthorUpdateRequest
import com.ggardet.modulith.author.repository.AuthorRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val authorMapper: AuthorMapper
) {
    fun findById(id: Long): Author? = authorRepository.findById(id).orElse(null)

    fun getAllAuthors(pageable: Pageable): Page<Author> = authorRepository.findAll(pageable)

    @Transactional
    fun createAuthor(request: AuthorCreateRequest): Author {
        val author = authorMapper.toEntity(request)
        val savedAuthor = authorRepository.save(author)
        eventPublisher.publishEvent(AuthorCreated(savedAuthor.id, authorMapper.getFullName(savedAuthor)))
        return savedAuthor
    }

    @Transactional
    fun updateAuthor(id: Long, request: AuthorUpdateRequest): Author {
        val author = findById(id) ?: throw IllegalArgumentException("Author not found")
        val updatedAuthor = authorMapper.toUpdatedEntity(author, request)
        val savedAuthor = authorRepository.save(updatedAuthor)
        eventPublisher.publishEvent(AuthorUpdated(savedAuthor.id, authorMapper.getFullName(savedAuthor)))
        return savedAuthor
    }

    @Transactional
    fun deleteAuthor(id: Long) {
        val author = findById(id) ?: throw IllegalArgumentException("Author not found")
        eventPublisher.publishEvent(AuthorDeleted(author.id, authorMapper.getFullName(author)))
        authorRepository.deleteById(id)
    }
}
