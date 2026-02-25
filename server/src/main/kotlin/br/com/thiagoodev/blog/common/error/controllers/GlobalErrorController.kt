package br.com.thiagoodev.blog.common.error.controllers

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import br.com.thiagoodev.blog.common.error.extensions.toResponseEntity
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import tools.jackson.databind.ObjectMapper

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

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "User not found",
            status = HttpStatus.NOT_FOUND.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(
        ex: DataIntegrityViolationException,
    ): ResponseEntity<ErrorResponseDto> {
        return ErrorResponseDto(
            message = ex.message ?: "Data invalid",
            status = HttpStatus.BAD_REQUEST.value()
        ).toResponseEntity()
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
    ): ResponseEntity<List<Map<String, String>>> {
        val fieldErrors: List<FieldError> = ex.bindingResult.fieldErrors
        val errorBody: List<Map<String, String>> =
            fieldErrors.map { field ->
                mapOf(
                    "field" to field.field,
                    "error" to (field.defaultMessage ?: "Invalid field")
                )
        }

        return ResponseEntity.badRequest().body(errorBody)
    }
}