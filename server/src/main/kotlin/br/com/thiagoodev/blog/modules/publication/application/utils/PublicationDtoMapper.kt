package br.com.thiagoodev.blog.modules.publication.application.utils

import br.com.thiagoodev.blog.modules.publication.application.dtos.PublicationDto
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import br.com.thiagoodev.blog.modules.publication.domain.utils.SlugFactory
import br.com.thiagoodev.blog.modules.publication.domain.value_objects.Tag

fun PublicationDto.toPublication(): Publication {
    return Publication(
        title = title,
        description = description,
        text = text,
        tags = tags.map { Tag.from(it) }.toMutableSet(),
        slug = SlugFactory(title).generate(),
    )
}