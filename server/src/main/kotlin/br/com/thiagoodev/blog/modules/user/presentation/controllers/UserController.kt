package br.com.thiagoodev.blog.modules.user.presentation.controllers

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.user.application.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService,
) {
    @GetMapping("/me")
    fun getCurrentlyLoggedUser(
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<User> {
        val token: String = getToken(token)
        val email: String = jwtService.getSubject(token)
        val user: User = userService.findByEmail(email)

        return ResponseEntity.ok(user)
    }

    private fun getToken(token: String): String {
        return token.replace("Bearer", "")
    }

    @PostMapping("/")
    fun create(@Valid @RequestBody dto: CreateUserDto): ResponseEntity<User> {
        val user: User = userService.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}