package br.com.thiagoodev.blog.modules.publication.application.services

import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import br.com.thiagoodev.blog.modules.publication.infrastructure.repositories.PublicationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PublicationService(private val publicationRepository: PublicationRepository) {
    fun getAll(page: Int = 1, size: Int = 10): Page<Publication> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pageable = PageRequest.of(page, size, sort)
        return this.publicationRepository.findAll(pageable)
    }
}