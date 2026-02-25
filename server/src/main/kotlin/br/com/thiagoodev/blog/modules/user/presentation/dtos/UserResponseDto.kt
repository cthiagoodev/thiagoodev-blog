package br.com.thiagoodev.blog.modules.user.presentation.dtos

import java.time.LocalDateTime
import java.util.UUID

data class UserResponseDto(
    val uuid: UUID?,
    val name: String,
    val email: String,
    val permissions: List<String>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
) {
    companion object
}
