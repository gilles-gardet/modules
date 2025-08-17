package com.ggardet.modulith.user.service

import com.ggardet.modulith.user.User
import com.ggardet.modulith.user.UserCreated
import com.ggardet.modulith.user.repository.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun existsByUsername(username: String): Boolean = userRepository.existsByUsername(username)

    fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    @Transactional
    fun createUser(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): User {
        require(!existsByUsername(username)) { "Username already exists" }
        require(!existsByEmail(email)) { "Email already exists" }
        val encodedPassword = BCryptPasswordEncoder().encode(password)
        val user = User(
            username = username,
            email = email,
            password = encodedPassword,
            firstName = firstName,
            lastName = lastName
        )
        val savedUser = userRepository.save(user)
        eventPublisher.publishEvent(
            UserCreated(
                userId = savedUser.id,
                username = savedUser.username,
                email = savedUser.email
            )
        )
        return savedUser
    }
}
