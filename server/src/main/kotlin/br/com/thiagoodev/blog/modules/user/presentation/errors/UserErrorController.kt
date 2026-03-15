package br.com.thiagoodev.blog.modules.user.presentation.errors

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.presentation.controllers.UserController
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(
    basePackageClasses = [UserController::class],
)
@Order(Ordered.HIGHEST_PRECEDENCE)
class UserErrorController {
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        ex: UserAlreadyExistsException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "User already exists",
            status = HttpStatus.CONFLICT.value()
        ).toResponseEntity()
    }
}