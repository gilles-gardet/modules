package com.ggardet.modulith.author.repository

import com.ggardet.modulith.author.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long>
