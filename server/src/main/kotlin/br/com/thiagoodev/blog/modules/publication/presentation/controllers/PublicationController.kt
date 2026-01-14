package br.com.thiagoodev.blog.modules.publication.presentation.controllers

import br.com.thiagoodev.blog.modules.publication.application.dtos.CreatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.services.PublicationService
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/publications")
class PublicationController(private val publicationService: PublicationService) {
    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<Page<Publication>> {
        val publications = publicationService.getAll(page, size)
        return ResponseEntity.ok(publications)
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody dto: CreatePublicationDto): ResponseEntity<Publication> {
        val publication = publicationService.create(dto)
        return ResponseEntity.ok(publication);
    }
}