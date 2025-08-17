package com.ggardet.modulith.core.event

import org.slf4j.LoggerFactory
import org.springframework.modulith.events.CompletedEventPublications
import org.springframework.modulith.events.IncompleteEventPublications
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration.ofSeconds

@Component
class EventPublications(
    private val incompleteEventPublications: IncompleteEventPublications,
    private val completedEventPublications: CompletedEventPublications
) {
    private val log = LoggerFactory.getLogger(EventPublications::class.java)

    @Scheduled(fixedRate = 60_000, initialDelay = 1_000)
    fun reSubmitIncompleteEventsOlderThan60s() {
        log.info("Resubmit incomplete publications")
        incompleteEventPublications.resubmitIncompletePublicationsOlderThan(ofSeconds(60))
    }

    @Scheduled(fixedRate = 60_000, initialDelay = 1_000)
    fun listCompleteEvents() {
        val numberOfPublications = completedEventPublications.findAll().size
        log.info("List of {} complete publications", numberOfPublications)
        completedEventPublications.findAll().forEach { event ->
            log.info("Event Id={}", event.identifier)
            log.info("Event={}", event.event)
            log.info("Publication Date={}", event.publicationDate)
            log.info("Completion Date={}", event.completionDate)
        }
    }
}
