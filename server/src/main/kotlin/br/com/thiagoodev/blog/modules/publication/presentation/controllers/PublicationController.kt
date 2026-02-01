package br.com.thiagoodev.blog.modules.publication.presentation.controllers

import br.com.thiagoodev.blog.modules.publication.application.dtos.CreatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.dtos.UpdatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.services.PublicationService
import br.com.thiagoodev.blog.modules.publication.application.services.PublicationViewService
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
@RequestMapping("/publications")
class PublicationController(
    private val publicationService: PublicationService,
    private val publicationViewService: PublicationViewService,
) {
    private val cookieMaxAge: Int = 24 * 60 * 60
    private val cookiePath: String = "/"
    private val cookieHttpOnly: Boolean = true
    private val viewedCookiePrefix: String = "viewed_post_"

    @GetMapping
    fun getAll(pageable: Pageable): ResponseEntity<Page<Publication>> {
        val publications = publicationService.getAll(pageable)
        return ResponseEntity.ok(publications)
    }

    @GetMapping("/{uuid}")
    fun getByUUID(
        @PathVariable uuid: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): ResponseEntity<Publication> {
        val publication = publicationService.getByUUID(uuid)
        setViewedCookie(uuid, request, response)
        return ResponseEntity.ok(publication)
    }

    private fun setViewedCookie(
        uuid: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val cookieName = getCookieName(uuid)

        if(!hasViewedCookie(request, cookieName)) {
            val cookie = createViewedCookie(cookieName)
            response.addCookie(cookie)
            publicationViewService.sendPublicationViewedEvent(uuid)
        }
    }

    private fun getCookieName(uuid: String) = "$viewedCookiePrefix$uuid"

    private fun hasViewedCookie(
        request: HttpServletRequest,
        cookieName: String,
    ): Boolean = request.cookies.any { it.name == cookieName }

    private fun createViewedCookie(cookieName: String): Cookie {
        return Cookie(cookieName, "true").apply {
            maxAge = cookieMaxAge
            path = cookiePath
            isHttpOnly = cookieHttpOnly
        }
    }

    @GetMapping("/featured")
    fun getFeaturedPublication(): ResponseEntity<Publication> {
        val publication: Publication = publicationService.getFeaturedPublication()
        return ResponseEntity.ok(publication)
    }

    @GetMapping("/on-current-week")
    fun getPublicationsOnCurrentWeek(): ResponseEntity<List<Publication>> {
        val publications: List<Publication> = publicationService.getPublicationsOnCurrentWeek()
        return ResponseEntity.ok(publications)
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