package dk.lldata.axon.banking.transfer

import org.axonframework.eventsourcing.EventSourcingHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class LoggingEventHandler {
  val logger = LoggerFactory.getLogger(LoggingEventHandler::class.java)

  @EventSourcingHandler
  fun on(event : Any) {
    logger.info("event {}", event)
  }
}