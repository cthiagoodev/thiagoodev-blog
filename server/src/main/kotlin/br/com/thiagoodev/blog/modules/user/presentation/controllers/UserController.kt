package br.com.thiagoodev.blog.modules.user.presentation.controllers

import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.presentation.utils.toUser
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import br.com.thiagoodev.blog.modules.user.presentation.dtos.UserResponseDto
import br.com.thiagoodev.blog.modules.user.presentation.utils.fromUser
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @GetMapping("/me")
    fun getCurrentlyLoggedUser(
        @AuthenticationPrincipal userDetails: UserDetailsAdapter,
    ): ResponseEntity<UserResponseDto> {
        val response = UserResponseDto.fromUser(userDetails.getUser())
        return ResponseEntity.ok(response)
    }

    @PostMapping("/create")
    fun create(
        @Valid @RequestBody dto: CreateUserDto,
    ): ResponseEntity<UserResponseDto> {
        val user: User = userService.create(dto.toUser())
        val response = UserResponseDto.fromUser(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}