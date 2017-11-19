package dk.lldata.axon.banking.transfer

import dk.lldata.axon.banking.coreapi.*
import mu.KotlinLogging
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventhandling.saga.EndSaga
import org.axonframework.eventhandling.saga.SagaEventHandler
import org.axonframework.eventhandling.saga.StartSaga
import org.axonframework.spring.stereotype.Saga
import org.springframework.beans.factory.annotation.Autowired

private val logger = KotlinLogging.logger {}

@Saga
class MoneyTransferSaga {
  @Autowired @Transient
  var commandGateway : CommandGateway? = null

  private var targetAccount: String? = null

  @StartSaga
  @SagaEventHandler(associationProperty = "transferId")
  fun on(event : MoneyTransferRequestedEvent) {
    this.targetAccount = event.targetAccountId
    logger.info("Money transfer requested from={} to={} amount={}", event.sourceAccountId, event.targetAccountId, event.amount)
    try {
      commandGateway!!.sendAndWait<WithdrawMoneyCommand>(WithdrawMoneyCommand(event.sourceAccountId, event.transferId, event.amount))
      logger.info("Money withdraw command success tx={}", event.transferId)
    } catch (e : Exception) {
      commandGateway!!.send<CancelMoneyTransferCommand>(CancelMoneyTransferCommand(event.transferId))
    }
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

  @EndSaga
  @SagaEventHandler(associationProperty = "transferId")
  fun on(event : MoneyTransferCancelledEvent) {
    // alternative to @EndSaga annotation SagaLifecycle.end()
  }

}