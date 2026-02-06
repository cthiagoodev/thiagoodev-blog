package br.com.thiagoodev.blog.modules.auth.presentation.errors

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import br.com.thiagoodev.blog.modules.auth.domain.exceptions.NotAuthenticatedException
import br.com.thiagoodev.blog.modules.auth.presentation.controllers.AuthController
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(
    basePackageClasses = [AuthController::class]
)
@Order(Ordered.HIGHEST_PRECEDENCE)
class AuthErrorController {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "User not found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(NotAuthenticatedException::class)
    fun handleNotAuthenticatedException(
        ex: NotAuthenticatedException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "User not authenticated",
            status = HttpStatus.UNAUTHORIZED.value(),
        ).toResponseEntity()
    }
}