package br.com.thiagoodev.blog.modules.publication.presentation.controllers

import br.com.thiagoodev.blog.modules.publication.application.dtos.CreatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.dtos.UpdatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.services.PublicationService
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @GetMapping("/{uuid}")
    fun getByUUID(@PathVariable uuid: String): ResponseEntity<Publication> {
        val publication = publicationService.getByUUID(uuid)
        return ResponseEntity.ok(publication)
    }

    @PostMapping("/create")
    fun create(@Valid @RequestBody dto: CreatePublicationDto): ResponseEntity<Publication> {
        val publication = publicationService.create(dto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(publication)
    }

    @PutMapping("/{uuid}/update")
    fun update(
        @PathVariable uuid: String,
        @Valid @RequestBody dto: UpdatePublicationDto,
    ): ResponseEntity<Publication> {
        val publication = publicationService.update(uuid, dto)
        return ResponseEntity.ok(publication);
    }

    @DeleteMapping("/{uuid}/delete")
    fun delete(@PathVariable uuid: String): ResponseEntity<Unit> {
        publicationService.delete(uuid)
        return ResponseEntity.noContent().build()
    }
}