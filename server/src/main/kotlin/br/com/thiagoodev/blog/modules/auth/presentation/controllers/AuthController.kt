package br.com.thiagoodev.blog.modules.auth.presentation.controllers

import br.com.thiagoodev.blog.modules.auth.application.dtos.CredentialsDto
import br.com.thiagoodev.blog.modules.auth.application.dtos.TokenDto
import br.com.thiagoodev.blog.modules.auth.application.services.AuthService
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
    fun authentication(@Valid @RequestBody body: CredentialsDto): ResponseEntity<TokenDto> {
        val token = authService.authenticate(body)
        return ResponseEntity.ok(token)
    }
}