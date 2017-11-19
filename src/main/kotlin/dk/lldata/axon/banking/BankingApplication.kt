package dk.lldata.axon.banking

import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.spring.config.EnableAxon
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@EnableAxon
@SpringBootApplication
class BankingApplication {

  @Bean
  fun eventStorageEngine() = InMemoryEventStorageEngine()
}

fun main(args: Array<String>) {
  runApplication<BankingApplication>(*args)
}

