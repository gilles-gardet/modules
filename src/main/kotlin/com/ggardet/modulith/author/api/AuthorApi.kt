package com.ggardet.modulith.author.api

data class AuthorCreateRequest(
    val firstName: String,
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)

data class AuthorUpdateRequest(
    val firstName: String,
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)

data class AuthorCreated(val authorId: Long, val name: String)

data class AuthorUpdated(val authorId: Long, val name: String)

data class AuthorDeleted(val authorId: Long, val name: String)
