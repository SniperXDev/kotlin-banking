package dk.lldata.axon.banking

import dk.lldata.axon.banking.account.Account
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.ID
import dk.lldata.axon.banking.coreapi.WithdrawMoneyCommand
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.commandhandling.model.GenericJpaRepository
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.axonframework.spring.config.EnableAxon
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

@EnableAxon
@SpringBootApplication
class BankingApplication {

  @Bean
  fun eventStorageEngine() = InMemoryEventStorageEngine()

  @Bean
  fun axonTransactionManager(tx : PlatformTransactionManager) = SpringTransactionManager(tx)

  @Bean
  fun accountRepo(eventBus : EventBus) = GenericJpaRepository(entityManagerProvider(), Account::class.java, eventBus)

  @Bean
  fun entityManagerProvider() = ContainerManagedEntityManagerProvider()
}

fun main(args: Array<String>) {
  val cfg = runApplication<BankingApplication>(*args)
  val commandBus = cfg.getBean(CommandBus::class.java)
  commandBus.dispatch(asCommandMessage<CreateAccountCommand>(CreateAccountCommand("1234", 1000)))
  commandBus.dispatch(asCommandMessage<WithdrawMoneyCommand>(WithdrawMoneyCommand("1234", ID.uuid(), 500)))
  commandBus.dispatch(asCommandMessage<WithdrawMoneyCommand>(WithdrawMoneyCommand("1234", ID.uuid(), 500)))
}

