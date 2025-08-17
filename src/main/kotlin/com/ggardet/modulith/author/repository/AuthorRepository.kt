package com.ggardet.modulith.author.repository

import com.ggardet.modulith.author.domain.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long> {

    fun findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        firstName: String,
        lastName: String,
        pageable: Pageable
    ): Page<Author>

    fun findByNationality(nationality: String, pageable: Pageable): Page<Author>

    @Query("SELECT a FROM Author a WHERE a.birthYear BETWEEN :startYear AND :endYear")
    fun findByBirthYearBetween(startYear: Int, endYear: Int, pageable: Pageable): Page<Author>
}
