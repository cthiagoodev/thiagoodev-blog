package br.com.thiagoodev.blog.modules.publication.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "publications")
data class Publication(
    @Id
    val uui: UUID = UUID.randomUUID(),
    val title: String,
    val slug: String,
    val description: String,
    val text: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val deletedAt: LocalDateTime? = null,
)
