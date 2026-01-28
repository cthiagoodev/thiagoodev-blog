package br.com.thiagoodev.blog.modules.publication.presentation.erros

import br.com.thiagoodev.blog.modules.publication.domain.exceptions.PublicationAlreadyDeletedException
import br.com.thiagoodev.blog.modules.publication.domain.exceptions.PublicationNotFoundException
import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import br.com.thiagoodev.blog.modules.publication.presentation.controllers.PublicationController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(
    basePackageClasses = [PublicationController::class]
)
@Order(Ordered.HIGHEST_PRECEDENCE)
class PublicationErrorController {
    @ExceptionHandler(PublicationNotFoundException::class)
    fun handlePublicationNotFoundException(
        ex: PublicationNotFoundException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Publication not found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(PublicationAlreadyDeletedException::class)
    fun handlePublicationAlreadyDeletedException(
        ex: PublicationAlreadyDeletedException
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Publication already deleted",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }
}