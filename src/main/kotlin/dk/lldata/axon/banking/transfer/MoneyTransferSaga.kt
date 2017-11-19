package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.saga.EndSaga
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.SagaLifecycle
import org.axonframework.eventhandling.saga.StartSaga
import org.springframework.beans.factory.annotation.Autowired

class MoneyTransferSaga {

  @Autowired @Transient
  var commandGateway : CommandGateway? = null

  private var targetAccount: String? = null

  @StartSaga
  @SagaEventHandler(associationProperty = "transferId")
  fun on(event : MoneyTransferRequestedEvent) {
    this.targetAccount = event.targetAccountId
    commandGateway!!.send<WithdrawMoneyCommand>(WithdrawMoneyCommand(event.sourceAccountId, event.transferId, event.amount))
  }

  @SagaEventHandler(associationProperty = "txId", keyName = "transferId")
  fun on(event : MoneyWithdrawnEvent) {
    commandGateway!!.send<DepositMoneyCommand>(DepositMoneyCommand(this.targetAccount!!, event.txId, event.amount))
  }

  @SagaEventHandler(associationProperty = "txId", keyName = "transferId")
  fun on(event : MoneyDepositedEvent) {
    commandGateway!!.send<CompleteMoneyTransferCommand>(CompleteMoneyTransferCommand(event.txId))
  }

  @EndSaga
  @SagaEventHandler(associationProperty = "transferId")
  fun on(event : MoneyTransferCompletedEvent) {
    // alternative to @EndSaga annotation SagaLifecycle.end()
  }

}