package com.ggardet.modulith.user.repository

import com.ggardet.modulith.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun existsByUsername(username: String): Boolean

    fun existsByEmail(email: String): Boolean
}
