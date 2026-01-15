package br.com.thiagoodev.blog.modules.publication.application.services

import br.com.thiagoodev.blog.modules.publication.application.dtos.PublicationDto
import br.com.thiagoodev.blog.modules.publication.application.utils.toPublication
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import br.com.thiagoodev.blog.modules.publication.infrastructure.repositories.PublicationRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PublicationService(
    private val publicationRepository: PublicationRepository,
) {
    fun getAll(pageable: Pageable): Page<Publication> {
        val pageable = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.Direction.DESC,
            "createdAt",
        )

        return this.publicationRepository.findAll(pageable)
    }

    @Transactional
    fun create(dto: PublicationDto): Publication {
        val publication: Publication = dto.toPublication()
        return publicationRepository.save(publication)
    }

    fun update(id: String, dto: PublicationDto): Publication {
        val uuid = UUID.fromString(id)
        val publication: Publication = publicationRepository.findByIdOrNull(uuid)
            ?: throw RuntimeException("Publication not found")

        publication.apply {
            val newPublication: Publication = dto.toPublication()
            title = newPublication.title
            slug = newPublication.slug
            description = newPublication.description
            text = newPublication.text

            tags.clear()
            tags.addAll(newPublication.tags)
        }

        return publicationRepository.save(publication)
    }
}