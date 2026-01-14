package br.com.thiagoodev.blog.modules.publication.application.dtos

import jakarta.validation.constraints.NotBlank

data class CreatePublicationDto(
    @NotBlank(message = "The field title is mandatory")
    val title: String,
    @NotBlank(message = "The field description is mandatory")
    val description: String,
    val tags: List<String> = listOf(),
    val text: String?,
)
