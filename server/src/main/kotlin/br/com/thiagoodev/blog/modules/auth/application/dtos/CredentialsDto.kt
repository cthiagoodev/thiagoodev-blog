package br.com.thiagoodev.blog.modules.auth.application.dtos

import jakarta.validation.constraints.NotBlank

data class CredentialsDto(
    @NotBlank(message = "The field email is mandatory")
    val email: String,
    @NotBlank(message = "The field password is mandatory")
    val password: String
)
