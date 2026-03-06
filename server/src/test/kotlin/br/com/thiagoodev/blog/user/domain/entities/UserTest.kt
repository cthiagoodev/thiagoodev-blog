package br.com.thiagoodev.blog.user.domain.entities

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import net.datafaker.Faker
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UserTest {
    private val faker = Faker()
    private lateinit var tName: String
    private lateinit var tEmail: String
    private lateinit var tPassword: String

    @BeforeTest
    fun setup() {
        tName = faker.name().fullName()
        tEmail = faker.internet().emailAddress()
        tPassword = faker.credentials().password()
    }

    @Test
    fun `Should return true when the user is an admin`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword,
            permissions = mutableSetOf(Permission.ADMIN)
        )

        val isAdmin: Boolean = user.isAdmin()

        assertTrue(isAdmin)
    }

    @Test
    fun `Should return false when the user is not an admin`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword
        )

        val isAdmin: Boolean = user.isAdmin()

        assertFalse(isAdmin)
    }

    @Test
    fun `Should add a new permission to the user`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword
        )

        user.addPermission(Permission.READ)

        assertContains(user.permissions, Permission.READ)
    }

    @Test
    fun `Should not duplicate permissions when adding an existing permission`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword,
            permissions = mutableSetOf(Permission.READ)
        )

        user.addPermission(Permission.READ)

        assertContains(user.permissions, Permission.READ)
        assertEquals(1, user.permissions.size)
    }

    @Test
    fun `Should initialize with empty permissions when not provided`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword
        )

        assertTrue(user.permissions.isEmpty())
    }

    @Test
    fun `Should keep existing permissions when adding a new different permission`() {
        val user = User(
            name = tName,
            email = tEmail,
            password = tPassword,
            permissions = mutableSetOf(Permission.READ)
        )

        user.addPermission(Permission.ADMIN)

        assertEquals(2, user.permissions.size)
        assertContains(user.permissions, Permission.READ)
        assertContains(user.permissions, Permission.ADMIN)
    }
}