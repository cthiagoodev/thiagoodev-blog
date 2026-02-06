package br.com.thiagoodev.blog.modules.auth.application.services

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.auth.application.dtos.CredentialsDto
import br.com.thiagoodev.blog.modules.auth.application.dtos.TokenDto
import br.com.thiagoodev.blog.modules.auth.domain.exceptions.NotAuthenticatedException
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
) {
    fun authenticate(dto: CredentialsDto): TokenDto {
        val user: User = userRepository.findUniqueByEmail(dto.email)
            ?: throw UserNotFoundException()
        val userDetails = UserDetailsAdapter(user)

        val credentials = UsernamePasswordAuthenticationToken(
            userDetails.username,
            userDetails.password,
        );
        val auth = authenticationManager.authenticate(credentials)

        if(!auth.isAuthenticated) throw NotAuthenticatedException()

        val token: String = jwtService.buildToken(userDetails.username)
        return TokenDto(token)
    }
}