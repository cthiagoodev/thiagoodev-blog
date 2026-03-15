package br.com.thiagoodev.blog.user.presentation.utils

import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.presentation.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.presentation.utils.toUser
import net.datafaker.Faker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CreateUserDtoMapperTest {
    private val faker = Faker()

    @Test
    fun `Should map CreateUserDto to User and assign READ permission by default`() {
        val dto = CreateUserDto(
            faker.name().fullName(),
            faker.internet().emailAddress(),
            faker.credentials().password(),
        )

        val user = dto.toUser()

        assertEquals(dto.name, user.name)
        assertEquals(dto.email, user.email)
        assertEquals(dto.password, user.password)
        assertTrue(user.permissions.contains(Permission.READ))
        assertEquals(1, user.permissions.size)
    }
}