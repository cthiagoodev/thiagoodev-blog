package br.com.thiagoodev.blog.modules.user.infrastructure.security

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsAdapter(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return user.permissions.toGrantedAuthorities()
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email
}