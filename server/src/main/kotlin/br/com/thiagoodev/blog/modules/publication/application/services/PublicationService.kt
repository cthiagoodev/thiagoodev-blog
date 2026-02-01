package br.com.thiagoodev.blog.modules.publication.application.services

import br.com.thiagoodev.blog.modules.publication.application.dtos.CreatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.dtos.UpdatePublicationDto
import br.com.thiagoodev.blog.modules.publication.application.utils.toPublication
import br.com.thiagoodev.blog.modules.publication.domain.entities.Publication
import br.com.thiagoodev.blog.modules.publication.domain.exceptions.PublicationAlreadyDeletedException
import br.com.thiagoodev.blog.modules.publication.domain.exceptions.PublicationNotFoundException
import br.com.thiagoodev.blog.modules.publication.domain.utils.SlugFactory
import br.com.thiagoodev.blog.modules.publication.domain.value_objects.Tag
import br.com.thiagoodev.blog.modules.publication.infrastructure.repositories.PublicationRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.UUID

@Service
class PublicationService(private val publicationRepository: PublicationRepository) {
    fun getAll(pageable: Pageable): Page<Publication> {
        val pageable = PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.by(Sort.Direction.DESC, "createdAt"),
        )

        return publicationRepository.findAllByDeletedAtIsNull(pageable)
    }

    fun getByUUID(uuid: String): Publication {
        return publicationRepository.findByUuidAndDeletedAtIsNull(UUID.fromString(uuid))
            ?: throw PublicationNotFoundException()
    }

    fun getFeaturedPublication(): Publication {
        return publicationRepository.findMostViewed()
            ?: throw PublicationNotFoundException()
    }

    fun getPublicationsOnCurrentWeek(): List<Publication> {
        val today = LocalDate.now()
        val start = today.with(TemporalAdjusters
            .previousOrSame(DayOfWeek.SUNDAY))
            .atStartOfDay()
        val end = today.with(TemporalAdjusters
            .nextOrSame(DayOfWeek.SATURDAY))
            .atTime(LocalTime.MAX)

        return publicationRepository.findAllByCreatedAtBetweenAndDeletedAtIsNull(start, end)
    }

    @Transactional
    fun create(dto: CreatePublicationDto): Publication {
        val publication: Publication = dto.toPublication()
        return publicationRepository.save(publication)
    }

    @Transactional
    fun update(uuid: String, dto: UpdatePublicationDto): Publication {
        val uuid = UUID.fromString(uuid)
        val publication: Publication = publicationRepository.findByUuidAndDeletedAtIsNull(uuid)
            ?: throw PublicationNotFoundException()

        publication.apply {
            dto.title?.let {
                title = it
                slug = SlugFactory(it).generate()
            }

            dto.description?.let { description = it }
            dto.text?.let { text = it }
            dto.image?.let { image = it }

            dto.tags?.let {
                tags.clear()
                tags.addAll(
                it
                    .mapNotNull { tag -> Tag.from(tag) }
                    .toMutableSet()
                )
            }

        }

        return publicationRepository.save(publication)
    }

    @Transactional
    fun delete(uuid: String): Publication {
        val uuid = UUID.fromString(uuid)
        val publication: Publication = publicationRepository.findByIdOrNull(uuid)
            ?: throw PublicationNotFoundException()

        if(publication.isDeleted) {
            throw PublicationAlreadyDeletedException()
        } else {
            publication.delete()
        }

        return publicationRepository.save(publication)
    }
}