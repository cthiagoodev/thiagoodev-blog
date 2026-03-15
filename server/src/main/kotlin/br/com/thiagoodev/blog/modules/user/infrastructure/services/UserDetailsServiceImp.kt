package br.com.thiagoodev.blog.modules.user.infrastructure.services

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

/**
 * Implementação do Contrato de Fornecimento de Usuários do Spring Security.
 *
 * Integração com o AuthenticationManager:
 * Esta classe é "descoberta" magicamente pelo Spring Security por implementar a interface [UserDetailsService].
 * Quando ocorre uma tentativa de Login lá no `AuthService`, o `AuthenticationManager` não faz
 * as consultas de banco de dados diretamente; ele invoca a função [loadUserByUsername] desta classe.
 *
 * Objetivo crítico:
 * É responsabilidade desta função encontrar o usuário no banco de dados com base na identificação
 * (username/email) e devolvê-lo na forma de um `UserDetailsAdapter`. O Spring precisa desse retorno
 * para poder ler o método `getPassword()` (que conterá o Hash BCrypt) a fim de realizar a validação final
 * das credenciais.
 */
@Service
class UserDetailsServiceImp(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findUniqueByEmail(username)
            ?: throw UserNotFoundException()

        return UserDetailsAdapter(user)
    }
}