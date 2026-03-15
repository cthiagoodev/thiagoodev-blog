package br.com.thiagoodev.blog.modules.user.infrastructure.security

import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Funções de extensão para traduzir o enum de [Permission] do domínio para a interface [GrantedAuthority].
 *
 * Padrão "ROLE_":
 * O Spring Security possui uma convenção histórica fortemente enraizada no prefixo "ROLE_".
 * Muitas das validações internas de rotas e métodos (como `hasRole('ADMIN')`) automaticamente prefixam
 * a string com "ROLE_" antes de fazer a verificação. Mapear nossos enums puros (ex: "ADMIN") para
 * "ROLE_ADMIN" assegura que a autorização nativa do Spring funcione sem dores de cabeça.
 */
fun Permission.toGrantedAuthority(): GrantedAuthority {
    return SimpleGrantedAuthority("ROLE_${toString()}")
}

fun Set<Permission>.toGrantedAuthorities(): List<GrantedAuthority> {
    return map { it.toGrantedAuthority() }
}