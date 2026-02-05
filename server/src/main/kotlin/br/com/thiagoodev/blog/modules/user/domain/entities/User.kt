package br.com.thiagoodev.blog.modules.user.domain.entities

import br.com.thiagoodev.blog.modules.user.domain.value_objects.Permission
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val uuid: UUID? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false, unique = true)
    var email: String,
    @Column(nullable = false)
    var password: String,
    @ElementCollection
    @Column(name = "permission", nullable = false)
    @CollectionTable(
        name = "user_permissions",
        joinColumns = [JoinColumn(name = "user_uuid")],
        uniqueConstraints = [UniqueConstraint(columnNames = ["user_uuid", "permission"])],
    )
    @Enumerated(EnumType.STRING)
    var permissions: MutableSet<Permission> = mutableSetOf(),
    @Column(nullable = false, updatable = false)
    @CreatedDate
    val createdAt: LocalDateTime? = null,
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null,
    val deletedAt: LocalDateTime? = null,
)