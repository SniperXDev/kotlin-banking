package dk.lldata.axon.banking

import dk.lldata.axon.banking.account.Account
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.model.GenericJpaRepository
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider
import org.axonframework.common.jpa.EntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.saga.repository.jpa.JpaSagaStore
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine
import org.axonframework.serialization.xml.XStreamSerializer
import org.axonframework.spring.config.EnableAxon
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.transaction.PlatformTransactionManager

@EnableAxon
@SpringBootApplication
//@EntityScan(basePackages = arrayOf("org.axonframework", "dk.lldata"))
//@EntityScan(basePackages = arrayOf("dk.lldata"))
class BankingApplication {

  @Bean
  fun eventStorageEngine(emp: EntityManagerProvider, tx : TransactionManager) =
      JpaEventStorageEngine(emp, tx)

//  @Bean
//  fun sagaStore(emp : EntityManagerProvider) = JpaSagaStore(emp)

  @Bean
  fun axonTransactionManager(tx : PlatformTransactionManager) = SpringTransactionManager(tx)

  @Bean
  fun accountRepo(eventBus : EventBus) = GenericJpaRepository(entityManagerProvider(), Account::class.java, eventBus)

  @Bean
  fun entityManagerProvider() = ContainerManagedEntityManagerProvider()

  @Bean
  fun serializer() = XStreamSerializer()
}

fun main(args: Array<String>) {
  val cfg = runApplication<BankingApplication>(*args)
  val commandBus = cfg.getBean(CommandBus::class.java)
  //commandBus.dispatch(asCommandMessage<CreateAccountCommand>(CreateAccountCommand("a1", 1000)), LoggingCallback.INSTANCE)
  //commandBus.dispatch(asCommandMessage<CreateAccountCommand>(CreateAccountCommand("a2", 1000)), LoggingCallback.INSTANCE)
  //commandBus.dispatch(asCommandMessage<RequestMoneyTransferCommand>(RequestMoneyTransferCommand(ID.uuid(), "a1", "a2", 4000)), LoggingCallback.INSTANCE)
}

