package br.com.thiagoodev.blog.auth.presentation.dtos

import br.com.thiagoodev.blog.modules.auth.presentation.dtos.CredentialsDto
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertTrue

class CredentialsDtoTest {

    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    fun `should pass validation when email and password are provided`() {
        val dto = CredentialsDto(
            email = "thiago@email.com",
            password = "senha-super-secreta"
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isEmpty())
    }

    @Test
    fun `should fail validation when email is blank`() {
        val dto = CredentialsDto(
            email = "   ",
            password = "senha-super-secreta"
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isNotEmpty())
        assertTrue(violations.any { it.message == "The field email is mandatory" })
    }

    @Test
    fun `should fail validation when password is blank`() {
        val dto = CredentialsDto(
            email = "thiago@email.com",
            password = ""
        )

        val violations = validator.validate(dto)

        assertTrue(violations.isNotEmpty())
        assertTrue(violations.any { it.message == "The field password is mandatory" })
    }
}