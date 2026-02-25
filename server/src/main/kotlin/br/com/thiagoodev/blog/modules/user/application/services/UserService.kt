package br.com.thiagoodev.blog.modules.user.application.services

import br.com.thiagoodev.blog.modules.user.domain.entities.User
import br.com.thiagoodev.blog.modules.user.domain.exceptions.UserAlreadyExistsException
import br.com.thiagoodev.blog.modules.user.infrastructure.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    @Transactional
    fun create(newUser: User): User {
        if(userRepository.existsByEmail(newUser.email)) {
            throw UserAlreadyExistsException("User on e-mail already exists")
        }

        val user = newUser.apply {
            password = hashPassword(password)
        }

        return userRepository.save(user)
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }
}