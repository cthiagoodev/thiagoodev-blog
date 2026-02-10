package br.com.thiagoodev.blog.modules.user.application.services

import br.com.thiagoodev.blog.modules.user.application.dtos.CreateUserDto
import br.com.thiagoodev.blog.modules.user.application.utils.toUser
import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserNotFoundException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun findByEmail(email: String): User {
        if(email.isBlank()) {
            throw IllegalArgumentException("Email cannot be blank")
        }

        return userRepository.findUniqueByEmail(email) ?: throw UserNotFoundException()
    }

    @Transactional
    fun create(dto: CreateUserDto): User {
        try {
            val user = dto.toUser().apply {
                password = hashPassword(password)
            }
            return userRepository.save(user)
        } catch (error: DataIntegrityViolationException) {
            throw UserAlreadyExistsException("User on e-mail already exists")
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}