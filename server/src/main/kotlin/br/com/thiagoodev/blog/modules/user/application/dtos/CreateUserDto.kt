package br.com.thiagoodev.blog.modules.user.application.dtos

import jakarta.validation.constraints.NotBlank

data class CreateUserDto(
    @NotBlank(message = "The field name is mandatory")
    val name: String,
    @NotBlank(message = "The field email is mandatory")
    val email: String,
    @NotBlank(message = "The field password is mandatory")
    val password: String,
)
