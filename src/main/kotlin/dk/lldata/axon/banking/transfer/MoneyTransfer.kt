package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle.apply
import org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate
import javax.persistence.Entity
import javax.persistence.Id

@Aggregate
@Entity
class MoneyTransfer {
  @Id
  @AggregateIdentifier
  var transferId : String? = null

  constructor() {}

  @CommandHandler
  constructor(cmd : RequestMoneyTransferCommand) {
    apply(MoneyTransferRequestedEvent(cmd.transferId, cmd.sourceAccountId, cmd.targetAccountId, cmd.amount))
  }

  @CommandHandler
  fun handle(cmd : CompleteMoneyTransferCommand) {
    apply(MoneyTransferCompletedEvent(cmd.transferId))
  }

  @CommandHandler
  fun handle(cmd : CancelMoneyTransferCommand) {
    apply(MoneyTransferCancelledEvent(cmd.transferId))
  }

  @EventSourcingHandler
  fun on(event : MoneyTransferRequestedEvent) {
    this.transferId = event.transferId
  }

  @EventSourcingHandler
  fun on(event : MoneyTransferCompletedEvent) {
    markDeleted()
  }

  @EventSourcingHandler
  fun on(event : MoneyTransferCancelledEvent) {
    markDeleted()
  }
}