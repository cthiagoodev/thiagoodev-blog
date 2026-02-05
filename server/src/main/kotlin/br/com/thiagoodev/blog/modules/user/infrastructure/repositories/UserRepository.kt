package br.com.thiagoodev.blog.modules.user.infrastructure.repositories

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findUniqueByEmail(email: String): User?
}