package br.com.thiagoodev.blog.user.presentation.dtos

import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import jakarta.validation.Validation
import jakarta.validation.Validator
import net.datafaker.Faker
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateUserDtoTest {
    private val faker = Faker()
    private lateinit var validator: Validator

    @BeforeEach
    fun setUp() {
        validator = Validation.buildDefaultValidatorFactory().validator
    }

    @Test
    fun `Should fail validation when name is blank`() {
        val dto = CreateUserDto(
            name = "  ",
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
        )

         val violations = validator.validate(dto)

        assertTrue(violations.isNotEmpty())
        assertTrue(violations.any { it.message == "Name is required" })
    }

    @Test
    fun `Should fail validation when password is too short`() {
        val dto = CreateUserDto(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = "123",
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isNotEmpty())
        assertTrue(violations.any { it.message == "Password must be at least 8 characters long" })
    }

    @Test
    fun `Should pass validation when all fields are correct`() {
        val dto = CreateUserDto(
            name = faker.name().fullName(),
            email = faker.internet().emailAddress(),
            password = faker.credentials().password(),
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isEmpty())
    }
}