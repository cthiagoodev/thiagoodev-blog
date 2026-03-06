package br.com.thiagoodev.blog.user.infraestructure.security

import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.infrastructure.security.toGrantedAuthorities
import br.com.thiagoodev.blog.modules.user.infrastructure.security.toGrantedAuthority
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPermissionToGrantedAuthorityExtensionTest {
    @Test
    fun `Should convert Permission to GrantedAuthority with ROLE_ prefix`() {
        val permission = Permission.READ

        val grantedAuthority = permission.toGrantedAuthority()

        assertEquals("ROLE_READ", grantedAuthority.authority)
    }

    @Test
    fun `Should convert a set of Permissions to a list of GrantedAuthorities`() {
        val permissions = setOf(Permission.READ, Permission.ADMIN)

        val grantedAuthorities = permissions.toGrantedAuthorities()

        assertEquals(2, grantedAuthorities.size)
        assertEquals("ROLE_READ", grantedAuthorities[0].authority)
        assertEquals("ROLE_ADMIN", grantedAuthorities[1].authority)
    }
}