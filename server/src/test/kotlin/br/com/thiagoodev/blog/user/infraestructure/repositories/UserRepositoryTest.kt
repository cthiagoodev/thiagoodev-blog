package br.com.thiagoodev.blog.user.infrastructure.repositories

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@TestPropertySource(properties = [
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
])
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    private val faker = Faker()
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
            permissions = mutableSetOf(Permission.READ, Permission.ADMIN)
        )
    }

    @Test
    fun `Should return user with fetched permissions when email exists`() {
        entityManager.persistAndFlush(user)
        entityManager.clear()

        val foundUser = userRepository.findUniqueByEmail(user.email)

        assertNotNull(foundUser)
        assertEquals(user.email, foundUser.email)
        assertEquals(2, foundUser.permissions.size)
        assertTrue(foundUser.permissions.contains(Permission.READ))
        assertTrue(foundUser.permissions.contains(Permission.ADMIN))
    }

    @Test
    fun `Should return null when finding by non-existent email`() {
        val foundUser = userRepository.findUniqueByEmail(faker.internet().emailAddress())

        assertNull(foundUser)
    }

    @Test
    fun `Should return true when checking existence of registered email`() {
        entityManager.persistAndFlush(user)

        val exists = userRepository.existsByEmail(user.email)

        assertTrue(exists)
    }

    @Test
    fun `Should return false when checking existence of unregistered email`() {
        val exists = userRepository.existsByEmail(faker.internet().emailAddress())

        assertFalse(exists)
    }
}