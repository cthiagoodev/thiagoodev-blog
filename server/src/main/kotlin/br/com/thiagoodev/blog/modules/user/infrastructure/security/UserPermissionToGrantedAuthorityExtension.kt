package br.com.thiagoodev.blog.modules.user.infrastructure.security

import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

fun Permission.toGrantedAuthority(): GrantedAuthority {
    return SimpleGrantedAuthority("ROLE_${toString()}")
}

fun Set<Permission>.toGrantedAuthorities(): List<GrantedAuthority> {
    return map { it.toGrantedAuthority() }
}