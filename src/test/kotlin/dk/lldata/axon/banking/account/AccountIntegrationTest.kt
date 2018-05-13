package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.balance.TransactionHistory
import dk.lldata.axon.banking.balance.TransactionHistoryEventHandler
import dk.lldata.axon.banking.balance.TransactionHistoryRepository
import dk.lldata.axon.banking.balance.TransactionHistoryRestController
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.DepositMoneyCommand
import org.assertj.core.api.Assertions.assertThat
import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.GenericCommandMessage
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventHandlingConfiguration
import org.axonframework.eventhandling.PropagatingErrorHandler
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.junit.Before
import org.junit.Test

class AccountIntegrationTest {
  val transactionHistoryRepository = TransactionHistoryRepositoryStub()
  val transactionHistoryEventHandler = TransactionHistoryEventHandler(transactionHistoryRepository)
  val transactionHistoryRestController = TransactionHistoryRestController(transactionHistoryRepository)
  var commandBus: CommandBus? = null

  @Before
  fun setup() {
    val eventhandlers = EventHandlingConfiguration()
        .configureListenerInvocationErrorHandler({ c -> PropagatingErrorHandler.instance() })
        .registerEventHandler({ c -> transactionHistoryEventHandler })

    val config = DefaultConfigurer.defaultConfiguration()
        .configureEmbeddedEventStore({ c -> InMemoryEventStorageEngine() })
        .configureAggregate(Account::class.java)
        .registerModule(eventhandlers)
        .buildConfiguration()
    config.start()

    this.commandBus = config.commandBus()
  }

  @Test
  fun accountAndBalance() {
    send(CreateAccountCommand("1234", 1000))
    send(DepositMoneyCommand("1234", "tx1", 500))

    assertThat(transactionHistoryRestController.history("1234")).usingElementComparatorIgnoringFields("id").containsExactly(
        TransactionHistory("1234", 0, "1234"),
        TransactionHistory("1234", 500, "tx1")
    )
  }

  fun send(cmd: Any) {
    commandBus!!.dispatch(GenericCommandMessage(cmd))
  }

}

class TransactionHistoryRepositoryStub : TransactionHistoryRepository {
  var sequence = 1L
  val db = linkedMapOf<String, List<TransactionHistory>>()

  override fun save(history: TransactionHistory) {
    val list = db[history.accountId]
    history.id = sequence++
    if (list == null) {
      db[history.accountId] = listOf(history)
    } else {
      db[history.accountId] = list + history
    }
  }

  override fun findByAccountId(accountId: String): List<TransactionHistory> {
    return db.getOrDefault(accountId, listOf())
  }

}

