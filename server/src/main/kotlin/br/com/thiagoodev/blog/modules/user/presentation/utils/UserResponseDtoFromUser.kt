package br.com.thiagoodev.blog.modules.user.presentation.utils

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.presentation.dtos.UserResponseDto

fun UserResponseDto.Companion.fromUser(user: User): UserResponseDto {
    return UserResponseDto(
        uuid = user.uuid,
        name = user.name,
        email = user.email,
        permissions = user.permissions.map { it.name },
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
        deletedAt = user.deletedAt,
    )
}