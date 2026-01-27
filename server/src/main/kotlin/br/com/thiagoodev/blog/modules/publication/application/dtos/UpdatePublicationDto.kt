package br.com.thiagoodev.blog.modules.publication.application.dtos

data class UpdatePublicationDto(
    val title: String?,
    val description: String?,
    val tags: List<String>?,
    val text: String?,
)
