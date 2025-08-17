package com.ggardet.modulith.author.model

import jakarta.validation.constraints.NotBlank

data class AuthorCreateRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)

data class AuthorUpdateRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    val nationality: String?,
    val birthYear: Int?,
    val biography: String?
)
