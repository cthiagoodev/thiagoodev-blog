package br.com.thiagoodev.blog.modules.publication.presentation.controllers

import br.com.thiagoodev.blog.modules.publication.application.dtos.PublicationDto
import br.com.thiagoodev.blog.modules.publication.application.services.PublicationService
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/publications")
class PublicationController(private val publicationService: PublicationService) {
    @GetMapping
    fun getAll(pageable: Pageable): ResponseEntity<Page<Publication>> {
        val publications = publicationService.getAll(pageable)
        return ResponseEntity.ok(publications)
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody dto: PublicationDto): ResponseEntity<Publication> {
        val publication = publicationService.create(dto)
        return ResponseEntity.ok(publication);
    }
}