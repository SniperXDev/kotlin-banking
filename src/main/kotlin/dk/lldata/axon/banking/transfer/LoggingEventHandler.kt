package dk.lldata.axon.banking.transfer

import mu.KotlinLogging
import org.axonframework.eventsourcing.EventSourcingHandler
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class LoggingEventHandler {
  @EventSourcingHandler
  fun on(event : Any) {
    logger.info("event {}", event)
  }
}