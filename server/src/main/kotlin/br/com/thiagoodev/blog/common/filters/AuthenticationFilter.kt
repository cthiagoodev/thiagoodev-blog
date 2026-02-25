package br.com.thiagoodev.blog.common.filters

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    @Value("\${spring.mvc.servlet.path:}")
    private val apiPrefix: String,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token: String? = getToken(request)

        if (!token.isNullOrBlank()) {
            try {
                val tokenSubject = jwtService.getSubject(token)
                val user = userRepository.findUniqueByEmail(tokenSubject)
                    ?: throw UserNotFoundException()
                val userDetails = UserDetailsAdapter(user)

                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    userDetails.password,
                    userDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authentication;
            } catch (e: Exception) {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }

    fun getToken(request: HttpServletRequest?): String? {
        if (request == null) return null
        val authorizationHeader = request.getHeader("Authorization") ?: return null
        return authorizationHeader.removePrefix("Bearer").trim()
    }
}