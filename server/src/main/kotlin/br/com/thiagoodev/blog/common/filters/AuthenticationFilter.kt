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

/**
 * O Filtro de Autenticação Stateless.
 * Esta classe intercepta rigorosamente TODAS as requisições que chegam no servidor.
 */
@Component
class AuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    @Value("\${spring.mvc.servlet.path:}")
    private val apiPrefix: String,
) : OncePerRequestFilter() {

    /**
     * Ciclo da Requisição Autenticada:
     * 1. Extrai o token do cabeçalho HTTP.
     * 2. Se o token for válido, o `JwtService` devolverá a identidade do usuário (e-mail/subject).
     * 3. Busca o usuário completo no banco de dados.
     * 4. Envelopa os dados em um `UsernamePasswordAuthenticationToken`.
     * * Detalhe de Segurança: Ao criarmos o token aqui, passamos `null` no campo "credentials" (senha).
     * Como a posse de um JWT criptograficamente válido já prova a autenticação, não precisamos da senha real.
     * 5. Injeta este token de autenticação no [SecurityContextHolder].
     * * É a partir do SecurityContextHolder que as anotações como `@AuthenticationPrincipal` nos Controllers
     * puxam a informação de quem está logado para processar as regras de negócio.
     */
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
                    null,
                    userDetails.authorities
                )

                SecurityContextHolder.getContext().authentication = authentication;
            } catch (e: Exception) {
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }

    /** Utilitário para sanitizar e extrair a string bruta do JWT do cabeçalho "Authorization: Bearer <token>" */
    fun getToken(request: HttpServletRequest?): String? {
        if (request == null) return null
        val authorizationHeader = request.getHeader("Authorization") ?: return null
        return authorizationHeader.removePrefix("Bearer").trim()
    }
}