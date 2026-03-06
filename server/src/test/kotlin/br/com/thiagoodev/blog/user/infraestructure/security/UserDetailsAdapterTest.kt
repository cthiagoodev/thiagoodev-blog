package br.com.thiagoodev.blog.user.infraestructure.security

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.infrastructure.security.UserDetailsAdapter
import net.datafaker.Faker
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UserDetailsAdapterTest {
    private val faker = Faker()

    private fun createFakeUser(deletedAt: LocalDateTime? = null): User {
        return User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
            permissions = mutableSetOf(Permission.ADMIN),
            deletedAt = deletedAt
        )
    }

    @Test
    fun `Should return correct credentials from User entity`() {
        val user = createFakeUser()
        val adapter = UserDetailsAdapter(user)

        assertEquals(user.email, adapter.username)
        assertEquals(user.password, adapter.password)
        assertEquals(user, adapter.getUser())
    }

    @Test
    fun `Should return enabled true when user is not deleted`() {
        val user = createFakeUser(deletedAt = null)
        val adapter = UserDetailsAdapter(user)

        assertTrue(adapter.isEnabled)
    }

    @Test
    fun `Should return enabled false when user has deletedAt date`() {
        val user = createFakeUser(deletedAt = LocalDateTime.now())
        val adapter = UserDetailsAdapter(user)

        assertFalse(adapter.isEnabled)
    }

    @Test
    fun `Should map permissions to authorities correctly`() {
        val user = createFakeUser()
        val adapter = UserDetailsAdapter(user)

        val authority = adapter.authorities.first()
        assertEquals("ROLE_ADMIN", authority.authority)
    }
}