package com.ggardet.modulith.user

import java.time.LocalDateTime

data class UserCreated(
    val userId: Long,
    val username: String,
    val email: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class UserLoginSuccessful(
    val username: String,
    val loginTimestamp: LocalDateTime = LocalDateTime.now()
)

data class UserLoginFailed(
    val username: String,
    val reason: String,
    val loginTimestamp: LocalDateTime = LocalDateTime.now()
)
