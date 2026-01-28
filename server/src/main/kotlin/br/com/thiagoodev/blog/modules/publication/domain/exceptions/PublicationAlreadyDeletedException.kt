package br.com.thiagoodev.blog.modules.publication.domain.exceptions

class PublicationAlreadyDeletedException(
    override val message: String? = "Publication already deleted"
) : RuntimeException(message)