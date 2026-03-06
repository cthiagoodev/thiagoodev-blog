package br.com.thiagoodev.blog.user.application.services

import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCrypt
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    private val faker = Faker()
    private lateinit var user: User

    @BeforeEach
    fun setup() {
        user = User(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password()
        )
    }

    @Test
    fun `Should return a new user with hashed password when the data is valid`() {
        val plainPassword = user.password

        `when`(userRepository.existsByEmail(user.email)).thenReturn(false)
        `when`(userRepository.save(user)).thenReturn(user)

        val createdUser = userService.create(user)

        assertNotEquals(plainPassword, createdUser.password)
        assertTrue(BCrypt.checkpw(plainPassword, createdUser.password))

        verify(userRepository, times(1)).existsByEmail(user.email)
        verify(userRepository, times(1)).save(user)
    }

    @Test
    fun `Should throw UserAlreadyExistsException when email is already registered`() {
        `when`(userRepository.existsByEmail(user.email)).thenReturn(true)

        assertFailsWith<UserAlreadyExistsException> {
            userService.create(user)
        }

        verify(userRepository, times(1)).existsByEmail(user.email)
        verify(userRepository, times(0)).save(user)
    }
}