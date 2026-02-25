package br.com.thiagoodev.blog.modules.auth.presentation.dtos

data class TokenResponseDto(
    val token: String,
    val expiresIn: Long,
) {
    companion object
}