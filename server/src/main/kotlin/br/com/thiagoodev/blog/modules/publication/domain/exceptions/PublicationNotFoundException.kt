package br.com.thiagoodev.blog.modules.publication.domain.exceptions

class PublicationNotFoundException(
    override val message: String? = "Publication not found"
) : RuntimeException(message)