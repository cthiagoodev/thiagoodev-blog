package br.com.thiagoodev.blog.common.error.controllers

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class GlobalErrorController {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Internal server error",
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ).toResponseEntity()
    }
}