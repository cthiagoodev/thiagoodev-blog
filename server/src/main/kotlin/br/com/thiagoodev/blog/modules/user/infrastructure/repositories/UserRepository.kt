package br.com.thiagoodev.blog.modules.user.infrastructure.repositories

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.permissions WHERE u.email = :email")
    fun findUniqueByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}