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
    /**
     * Executa o processo de autenticação e geração de token JWT.
     *
     * A Criptografia (Spring Security):
     * 1. Este método recebe o `email` e a `password` em TEXTO PLANO (ex: "senha123").
     * 2. Nós envelopamos esses dados crus em um `UsernamePasswordAuthenticationToken` e os entregamos
     * nas mãos do motor do framework: o [AuthenticationManager].
     * 3. Magia Oculta: O `AuthenticationManager` vai acionar o nosso `UserDetailsServiceImp` para buscar
     * o usuário no banco. O usuário virá do banco com a senha ENCRIPTADA (hash BCrypt).
     * 4. O `AuthenticationManager` usa o nosso bean `PasswordEncoder` para fazer o hash da senha em
     * texto plano que passamos e compara com o hash que veio do banco de dados.
     * 5. Se os hashes baterem, a autenticação é validada. Isso garante que a senha em texto plano nunca
     * seja comparada de forma insegura e não precisamos fazer a checagem manual de BCrypt aqui.
     */
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