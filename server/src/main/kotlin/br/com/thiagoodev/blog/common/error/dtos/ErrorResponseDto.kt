package br.com.thiagoodev.blog.common.error.dtos

data class ErrorResponseDto(
    val message: String,
    val status: Int,
)