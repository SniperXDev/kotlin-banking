package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.AccountCreatedEvent
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import dk.lldata.axon.banking.coreapi.MoneyWithdrawnEvent
import dk.lldata.axon.banking.coreapi.WithdrawMoneyCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler

class Account {
  constructor() {}

  @AggregateIdentifier
  private var accountId : String? = null
  var balance : Int = 0
  var overdraftLimit : Int = 0

  @CommandHandler
  constructor(cmd : CreateAccountCommand) {
    AggregateLifecycle.apply(AccountCreatedEvent(cmd.accountId, cmd.overdraftLimit))
  }

  @CommandHandler
  fun handle(cmd : WithdrawMoneyCommand) {
    if (balance + overdraftLimit >= cmd.amount) {
      AggregateLifecycle.apply(MoneyWithdrawnEvent(cmd.accountId, cmd.amount, balance - cmd.amount))
    } else {
      throw OverdraftLimitExceeded()
    }
  }

  @EventSourcingHandler
  fun on(event : AccountCreatedEvent) {
    this.accountId = event.accountId
    this.overdraftLimit = event.overdraftLimit
  }

  @EventSourcingHandler
  fun on(event : MoneyWithdrawnEvent) {
    this.balance = event.balance
  }
}

class OverdraftLimitExceeded : RuntimeException()