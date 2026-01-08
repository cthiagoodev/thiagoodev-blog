package br.com.thiagoodev.blog.modules.publication.infrastructure.repositories

import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PublicationRepository : JpaRepository<Publication, UUID>