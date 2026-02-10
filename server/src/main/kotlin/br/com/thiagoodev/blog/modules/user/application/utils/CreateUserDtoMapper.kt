package br.com.thiagoodev.blog.modules.user.application.utils

import br.com.thiagoodev.blog.modules.user.application.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission

fun CreateUserDto.toUser(): User {
    return User(
        name = name,
        email = email,
        password = password,
        permissions = mutableSetOf(Permission.READ)
    )
}