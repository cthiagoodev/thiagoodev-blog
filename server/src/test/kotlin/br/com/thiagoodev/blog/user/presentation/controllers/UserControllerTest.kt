package br.com.thiagoodev.blog.user.presentation.controllers

import br.com.thiagoodev.blog.common.services.JwtService
import br.com.thiagoodev.blog.modules.user.application.services.UserService
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import br.com.thiagoodev.blog.modules.user.presentation.controllers.UserController
import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import tools.jackson.databind.ObjectMapper
import kotlin.test.Test

@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var userService: UserService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @MockitoBean
    private lateinit var userRepository: UserRepository

    private val faker = Faker()
    private lateinit var user: User
    private lateinit var createUserDto: CreateUserDto

    @BeforeEach
    fun main() {
        val randomName = faker.name().fullName()
        val randomEmail = faker.internet().emailAddress()
        val randomPassword = faker.credentials().password()
        val randomCreatedDate = faker.timeAndDate().birthday().atStartOfDay()

        createUserDto = CreateUserDto(
            name = randomName,
            email = randomEmail,
            password = randomPassword
        )

        user = User(
            name = randomName,
            email = randomEmail,
            password = randomPassword,
            createdAt = randomCreatedDate,
        )
    }

    @Test
    fun `Should return 201 Created and the user data when payload is valid`() {
        `when`(userService.create(any())).thenReturn(user)

        val payload = objectMapper.writeValueAsString(createUserDto)

        mockMvc.post("/users/create") {
            contentType = MediaType.APPLICATION_JSON
            content = payload
        }.andExpect {
            status { isCreated() }
            jsonPath("$.name") { value(user.name) }
            jsonPath("$.email") { value(user.email) }
        }
    }
}