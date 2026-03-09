package br.com.thiagoodev.blog.auth.application.services

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.auth.application.services.AuthService
import br.com.thiagoodev.blog.modules.auth.domain.exceptions.NotAuthenticatedException
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import net.datafaker.Faker
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    @Mock
    private lateinit var jwtService: JwtService
    @Mock
    private lateinit var authenticationManager: AuthenticationManager
    @Mock
    private lateinit var userRepository: UserRepository
    @Mock
    private lateinit var authentication: Authentication
    @InjectMocks
    private lateinit var authService: AuthService

    private val faker = Faker()

    @Test
    fun `should authenticate user and return token successfully`() {
        val name = faker.name().fullName()
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()
        val generatedToken = faker.internet().uuid()
        val expiration = faker.number().numberBetween(3600L, 7200L)

        val user = User(
            name = name,
            email = email,
            password = password,
            permissions = mutableSetOf(),
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        `when`(jwtService.buildToken(email)).thenReturn(generatedToken)
        `when`(jwtService.getExpiration(generatedToken)).thenReturn(expiration)

        val result = authService.authenticate(email, password)

        assertEquals(generatedToken, result.accessToken)
        assertEquals(expiration, result.expiresIn)

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
    }

    @Test
    fun `should throw UserNotFoundException when email does not exist`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(null)

        assertFailsWith<UserNotFoundException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, never()).buildToken(anyString())
    }

    @Test
    fun `should throw NotAuthenticatedException when authentication is not successful`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(false)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)

        assertFailsWith<NotAuthenticatedException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, never()).buildToken(anyString())
    }

    @Test
    fun `should throw exception when authentication manager throws BadCredentialsException`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(BadCredentialsException::class.java)

        assertFailsWith<BadCredentialsException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, never()).buildToken(anyString())
    }

    @Test
    fun `should throw exception when jwtService fails to build token`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        `when`(jwtService.buildToken(email)).thenThrow(RuntimeException::class.java)

        assertFailsWith<RuntimeException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
        verify(jwtService, never()).getExpiration(anyString())
    }

    @Test
    fun `should throw exception when jwtService fails to get expiration`() {
        val email = faker.internet().emailAddress()
        val password = faker.credentials().password()
        val generatedToken = faker.internet().uuid()

        val user = User(
            name = faker.name().fullName(),
            email = email,
            password = password,
            permissions = mutableSetOf()
        )

        `when`(userRepository.findUniqueByEmail(email)).thenReturn(user)
        `when`(authentication.isAuthenticated).thenReturn(true)
        `when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java))).thenReturn(authentication)
        `when`(jwtService.buildToken(email)).thenReturn(generatedToken)
        `when`(jwtService.getExpiration(generatedToken)).thenThrow(RuntimeException::class.java)

        assertFailsWith<RuntimeException> {
            authService.authenticate(email, password)
        }

        verify(userRepository, times(1)).findUniqueByEmail(email)
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken::class.java))
        verify(jwtService, times(1)).buildToken(email)
        verify(jwtService, times(1)).getExpiration(generatedToken)
    }
}