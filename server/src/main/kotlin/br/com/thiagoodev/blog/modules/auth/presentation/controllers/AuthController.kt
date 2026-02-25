package br.com.thiagoodev.blog.modules.auth.presentation.controllers

import br.com.thiagoodev.blog.modules.auth.presentation.dtos.CredentialsDto
import br.com.thiagoodev.blog.modules.auth.presentation.dtos.TokenResponseDto
import br.com.thiagoodev.blog.modules.auth.application.services.AuthService
import br.com.thiagoodev.blog.modules.auth.presentation.utils.fromToken
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    @PostMapping("/")
    fun authentication(
        @Valid @RequestBody body: CredentialsDto,
    ): ResponseEntity<TokenResponseDto> {
        val token = authService.authenticate(body.email, body.password)
        val response = TokenResponseDto.fromToken(token)
        return ResponseEntity.ok(response)
    }
}