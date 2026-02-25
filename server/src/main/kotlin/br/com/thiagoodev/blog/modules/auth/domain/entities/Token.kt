package br.com.thiagoodev.blog.modules.auth.domain.entities

data class Token(
    val accessToken: String,
    val expiresIn: Long,
)
