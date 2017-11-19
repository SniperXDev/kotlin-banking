package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import java.util.*
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.Id

@Aggregate(repository = "accountRepo")
@Entity
class Account {
  constructor() {}

  @Id
  @AggregateIdentifier
  private var accountId : String? = null

  @Basic
  var balance : Int = 0
  @Basic
  var overdraftLimit : Int = 0

  @CommandHandler
  constructor(cmd : CreateAccountCommand) {
    AggregateLifecycle.apply(AccountCreatedEvent(cmd.accountId, cmd.overdraftLimit))
  }

  @CommandHandler
  fun handle(cmd : WithdrawMoneyCommand) {
    if (balance + overdraftLimit >= cmd.amount) {
      AggregateLifecycle.apply(MoneyWithdrawnEvent(cmd.accountId, cmd.txId, cmd.amount, balance - cmd.amount))
    } else {
      throw OverdraftLimitExceeded()
    }
  }

  @CommandHandler
  fun handle(cmd : DepositMoneyCommand) {
    AggregateLifecycle.apply(MoneyDepositedEvent(cmd.accountId, cmd.txId, cmd.amount, balance + cmd.amount))
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

  @EventSourcingHandler
  fun on(event : MoneyDepositedEvent) {
    this.balance = event.balance
  }
}

class OverdraftLimitExceeded : RuntimeException()