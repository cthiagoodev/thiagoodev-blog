package br.com.thiagoodev.blog.modules.publication.application.services

import br.com.thiagoodev.blog.modules.publication.infrastructure.events.PublicationViewedEvent
import br.com.thiagoodev.blog.modules.publication.infrastructure.repositories.PublicationRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PublicationViewService(
    private val repository: PublicationRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Async
    @EventListener
    @Transactional
    fun handlePublicationViewed(event: PublicationViewedEvent) {
        repository.incrementViews(event.uuid)
    }

    fun sendPublicationViewedEvent(id: String) {
        val uuid = UUID.fromString(id)
        eventPublisher.publishEvent(PublicationViewedEvent(uuid))
    }
}