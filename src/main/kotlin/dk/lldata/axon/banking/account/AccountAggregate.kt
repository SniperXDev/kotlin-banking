package dk.lldata.axon.banking.account

import dk.lldata.axon.banking.coreapi.AccountCreatedEvent
import dk.lldata.axon.banking.coreapi.CreateAccountCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler

class Account {
  constructor() {}

  @AggregateIdentifier
  private var accountId : String? = null

  @CommandHandler
  constructor(cmd : CreateAccountCommand) {
    AggregateLifecycle.apply(AccountCreatedEvent(cmd.accountId, cmd.overdraftLimit))
  }

  @EventSourcingHandler
  fun on(event : AccountCreatedEvent) {
    this.accountId = event.accountId
  }

}