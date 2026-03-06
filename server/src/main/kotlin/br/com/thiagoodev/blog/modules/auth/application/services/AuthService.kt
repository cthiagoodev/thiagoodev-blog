package br.com.thiagoodev.blog.modules.auth.application.services

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.auth.domain.entities.Token
import br.com.thiagoodev.blog.modules.auth.domain.exceptions.NotAuthenticatedException
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
) {
    fun authenticate(email: String, password: String): Token {
        val user: User = userRepository.findUniqueByEmail(email)
            ?: throw UserNotFoundException()

        val credentials = UsernamePasswordAuthenticationToken(
            email,
            password,
        )

        val auth = authenticationManager.authenticate(credentials)

        if(!auth.isAuthenticated) throw NotAuthenticatedException()

        val token: String = jwtService.buildToken(user.email)
        val expiresIn: Long = jwtService.getExpiration(token)

        return Token(
            accessToken = token,
            expiresIn = expiresIn
        )
    }
}