package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.*
import mu.KotlinLogging
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

private val logger = KotlinLogging.logger {}

@Aggregate
class Account {
  constructor() {}

  @AggregateIdentifier
  private var accountId: String? = null

  var balance: Int = 0
  var overdraftLimit: Int = 0

  @CommandHandler
  constructor(cmd: CreateAccountCommand) {
    AggregateLifecycle.apply(AccountCreatedEvent(cmd.accountId, cmd.accountId, cmd.overdraftLimit, 0))
  }

  @CommandHandler
  fun handle(cmd: WithdrawMoneyCommand) {
    if (balance + overdraftLimit >= cmd.amount) {
      logger.info("Money withdraw accepted account={} balance={}", accountId, balance)
      AggregateLifecycle.apply(MoneyWithdrawnEvent(cmd.accountId, cmd.txId, cmd.amount, balance - cmd.amount))
    } else {
      logger.info("Money withdraw denied account={} balance={}", accountId, balance)
      throw OverdraftLimitExceeded()
    }
  }

  @CommandHandler
  fun handle(cmd: DepositMoneyCommand) {
    AggregateLifecycle.apply(MoneyDepositedEvent(cmd.accountId, cmd.txId, cmd.amount, balance + cmd.amount))
  }

  @EventSourcingHandler
  fun on(event: AccountCreatedEvent) {
    this.accountId = event.accountId
    this.overdraftLimit = event.overdraftLimit
  }

  @EventSourcingHandler
  fun on(event: MoneyWithdrawnEvent) {
    this.balance = event.balance
    logger.info("Money deposited account={} balance={}", accountId, balance)
  }

  @EventSourcingHandler
  fun on(event: MoneyDepositedEvent) {
    this.balance = event.balance
    logger.info("Money deposited account={} balance={}", accountId, balance)
  }
}

class OverdraftLimitExceeded : RuntimeException()