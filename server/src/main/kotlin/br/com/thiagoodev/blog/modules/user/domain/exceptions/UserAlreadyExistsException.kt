package br.com.thiagoodev.blog.modules.user.domain.exceptions

class UserAlreadyExistsException(
    override val message: String? = "User already exists"
) : RuntimeException(message)