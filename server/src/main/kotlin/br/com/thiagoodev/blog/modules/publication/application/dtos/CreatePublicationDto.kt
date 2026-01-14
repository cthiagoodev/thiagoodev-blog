package br.com.thiagoodev.blog.modules.publication.application.dtos

import jakarta.validation.constraints.NotBlank

data class CreatePublicationDto(
    @NotBlank(message = "The field title is mandatory")
    val title: String,
    val description: String?,
    val tags: List<String>,
    val text: String,
    val talks: List<String>
)
