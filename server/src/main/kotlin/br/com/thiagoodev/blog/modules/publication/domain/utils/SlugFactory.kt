package br.com.thiagoodev.blog.modules.publication.domain.utils

class SlugFactory(private val value: String) {
    fun generate(): String {
        return value
            .lowercase()
            .replace(" ", "-")
    }
}