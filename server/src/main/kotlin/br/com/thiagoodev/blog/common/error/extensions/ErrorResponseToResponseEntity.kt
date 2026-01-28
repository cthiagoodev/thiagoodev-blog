package br.com.thiagoodev.blog.common.error.extensions

import br.com.thiagoodev.blog.common.error.dtos.ErrorResponseDto
import org.springframework.http.ResponseEntity

fun ErrorResponseDto.toResponseEntity(): ResponseEntity<ErrorResponseDto> {
    return ResponseEntity
        .status(status)
        .body(ErrorResponseDto(message, status))
}