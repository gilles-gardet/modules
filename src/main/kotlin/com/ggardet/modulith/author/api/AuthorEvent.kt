package com.ggardet.modulith.author.api

data class AuthorCreated(val authorId: Long, val name: String)

data class AuthorUpdated(val authorId: Long, val name: String)

data class AuthorDeleted(val authorId: Long, val name: String)
