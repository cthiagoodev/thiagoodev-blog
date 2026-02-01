package br.com.thiagoodev.blog.modules.publication.infrastructure.repositories

import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.UUID

interface PublicationRepository : JpaRepository<Publication, UUID> {
    fun findAllByDeletedAtIsNull(pageable: Pageable): Page<Publication>
    fun findByUuidAndDeletedAtIsNull(uuid: UUID): Publication?
    fun findAllByCreatedAtBetweenAndDeletedAtIsNull(start: LocalDateTime, end: LocalDateTime): List<Publication>
    @Modifying
    @Query("UPDATE Publication p SET p.viewsCount = p.viewsCount + 1 WHERE p.uuid = :uuid")
    fun incrementViews(uuid: UUID)
    @Query(
        value = "SELECT * FROM publications WHERE deleted_at IS NULL ORDER BY views_count DESC LIMIT 1",
        nativeQuery = true
    )
    fun findMostViewed(): Publication?
}