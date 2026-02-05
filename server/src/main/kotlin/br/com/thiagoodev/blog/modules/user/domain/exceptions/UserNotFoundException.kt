package br.com.thiagoodev.blog.modules.user.domain.exceptions

class UserNotFoundException(
    override val message: String? = "User not found",
) : RuntimeException(message)