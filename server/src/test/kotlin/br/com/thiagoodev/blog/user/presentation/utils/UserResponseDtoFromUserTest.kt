package br.com.thiagoodev.blog.user.presentation.utils

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import br.com.thiagoodev.blog.modules.user.presentation.dtos.UserResponseDto
import br.com.thiagoodev.blog.modules.user.presentation.utils.fromUser
import net.datafaker.Faker
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserResponseDtoFromUserTest {
    private val faker = Faker()

    @Test
    fun `Should map User entity to UserResponseDto correctly`() {
        val user = User(
            name = "Thiago",
            email = "thiago@email.com",
            password = "hashed_password",
            permissions = mutableSetOf(Permission.READ, Permission.ADMIN),
            createdAt = LocalDateTime.now()
        )

        val dto = UserResponseDto.fromUser(user)

        assertEquals(user.name, dto.name)
        assertEquals(user.email, dto.email)
        assertEquals(2, dto.permissions.size)
        assertTrue(dto.permissions.contains("READ"))
        assertTrue(dto.permissions.contains("ADMIN"))
        assertEquals(user.createdAt, dto.createdAt)
    }
}