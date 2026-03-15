package br.com.thiagoodev.blog.modules.user.infrastructure.security

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * O Padrão Adapter (Ponte) entre as Entidades de Domínio e o Spring Security.
 *
 * Arquitetura de Software:
 * O Spring Security é agnóstico à nossa aplicação. Ele não faz ideia de que possuímos uma classe
 * chamada `User`. O framework exige estritamente um objeto que implemente a interface [UserDetails].
 * Para não poluirmos nossa entidade `User` (que pertence à camada de Domínio) com dependências
 * de um framework externo (Spring), criamos esta classe. Ela "envelopa" nossa entidade e traduz
 * seus dados para a linguagem de segurança que o Spring entende.
 */
class UserDetailsAdapter(private val user: User) : UserDetails {

    /** Método utilitário para recuperarmos nossa entidade de domínio original em Controllers ou Filtros. */
    fun getUser(): User = user

    /** Informa ao framework se a conta está ativa. Usamos nossa lógica de "soft-delete" (deletedAt) para isso. */
    override fun isEnabled(): Boolean = user.deletedAt == null

    /** Fornece a lista de permissões formatadas para o mecanismo de Autorização do Spring. */
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return user.permissions.toGrantedAuthorities()
    }

    /**
     * IMPORTANTE: Esta função DEVE retornar a senha ENCRIPTADA armazenada no banco de dados.
     * O AuthenticationManager do Spring fará a chamada deste método durante o login para comparar
     * este Hash com a senha em texto plano que o usuário digitou na tela.
     */
    override fun getPassword(): String = user.password

    /** Fornece a credencial de identificação principal do usuário perante o framework (neste caso, o e-mail). */
    override fun getUsername(): String = user.email
}