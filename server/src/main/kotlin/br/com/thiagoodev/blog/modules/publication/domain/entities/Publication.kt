package br.com.thiagoodev.blog.modules.publication.domain.entities

import br.com.thiagoodev.blog.modules.publication.domain.value_objects.Tag
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "publications")
class Publication(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val uuid: UUID? = null,
    @Column(nullable = false)
    var title: String,
    @Column(unique = true, nullable = false)
    var slug: String,
    @Column(nullable = false)
    var description: String,
    @Column(nullable = true)
    var text: String?,
    @ElementCollection
    @CollectionTable(
        name = "publication_tags",
        joinColumns = [JoinColumn("publication_id")],
        uniqueConstraints = [UniqueConstraint(columnNames = ["publication_id", "tag"])]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    val tags: MutableSet<Tag> = mutableSetOf(),
    @ManyToMany
    @JoinTable(
        name = "publications_talks",
        joinColumns = [JoinColumn(name = "publication_id")],
        inverseJoinColumns = [JoinColumn(name = "talk_id")],
        uniqueConstraints = [UniqueConstraint(columnNames = ["publication_id", "talk_id"])],
    )
    val talks: MutableList<Talk> = mutableListOf(),
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
)