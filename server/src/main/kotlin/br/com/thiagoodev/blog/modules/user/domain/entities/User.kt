package br.com.thiagoodev.blog.modules.user.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val uuid: UUID? = null
)