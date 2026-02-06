package br.com.thiagoodev.blog.modules.auth.domain.exceptions

class NotAuthenticatedException(
    override val message: String? = "Not authenticated"
) : RuntimeException(message)