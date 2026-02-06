package br.com.thiagoodev.blog.common.filters

import br.com.thiagoodev.blog.common.config.security.SecurityConfiguration
import br.com.thiagoodev.blog.common.config.security.isPublicEndpoint
import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        filterChain: FilterChain?
    ) {
        if (!isPublic(request)) {
            filterChain?.doFilter(request, response)
            return
        }

        val token: String = getToken(request) ?: throw RuntimeException("Token not found")

        val tokenSubject = jwtService.getSubject(token)
        val user = userRepository.findUniqueByEmail(tokenSubject)
            ?: throw UserNotFoundException()
        val userDetails = UserDetailsAdapter(user)

        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            userDetails.getUsername(),
            null,
            userDetails.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication;
    }

    private fun isPublic(request: HttpServletRequest?): Boolean {
        if (request == null) return false
        return SecurityConfiguration.isPublicEndpoint(request.requestURI)
    }

    fun getToken(request: HttpServletRequest?): String? {
        if (request == null) return null
        val authorizationHeader = request.getHeader("Authorization") ?: return null
        return authorizationHeader.replace("Bearer", "")
    }
}